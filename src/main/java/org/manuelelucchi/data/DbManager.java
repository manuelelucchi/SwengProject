package org.manuelelucchi.data;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.EagerForeignCollection;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.manuelelucchi.common.DateUtils;
import org.manuelelucchi.models.*;

public class DbManager {
    private static DbManager _instance;

    private static String databaseUrl = "jdbc:sqlite:data.db";
    private ConnectionSource source;

    private Dao<Bike, Integer> bikes;
    private Dao<Card, Integer> cards;
    private Dao<Subscription, Integer> subscriptions;
    private Dao<Totem, Integer> totems;
    private Dao<Grip, Integer> grips;
    private Dao<Rental, Integer> rentals;

    private DbManager() {
    }

    public static DbManager getInstance() {
        if (_instance == null) {
            _instance = new DbManager();
        }
        return _instance;
    }

    public boolean ensureCreated() {
        if (!open())
            return false;

        try {
            TableUtils.createTableIfNotExists(source, Card.class);
            TableUtils.createTableIfNotExists(source, Subscription.class);
            TableUtils.createTableIfNotExists(source, Bike.class);
            TableUtils.createTableIfNotExists(source, Grip.class);
            TableUtils.createTableIfNotExists(source, Rental.class);
            TableUtils.createTableIfNotExists(source, Totem.class);

            cards = DaoManager.createDao(source, Card.class);
            subscriptions = DaoManager.createDao(source, Subscription.class);
            bikes = DaoManager.createDao(source, Bike.class);
            grips = DaoManager.createDao(source, Grip.class);
            rentals = DaoManager.createDao(source, Rental.class);
            totems = DaoManager.createDao(source, Totem.class);

            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean open() {
        try {
            source = new JdbcConnectionSource(databaseUrl);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean close() {
        try {
            source.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // @requires type != null
    // @requires password != null
    // @requires cardExpireDate != null
    public Subscription register(String password, SubscriptionType type, boolean isStudent, String cardCode,
            Date cardExpireDate, int cvv) {
        try {
            Subscription subscription = new Subscription(password, type, isStudent);
            Card card = new Card(cardCode, cvv, cardExpireDate);

            if (CardManager.getInstance().pay(card, subscription.getCost())) {
                subscription.setCard(card);
                if (type == SubscriptionType.year) {
                    subscription.activate();
                    subscriptions.create(subscription);
                    cards.createIfNotExists(card);
                }
                return subscription;
            } else {
                return null;
            }

        } catch (SQLException e) {
            return null;
        }
    }

    // @requires password != null
    public Subscription login(int code, String password) {
        try {
            var subscription = subscriptions.queryForId(code);
            if (subscription == null) {
                return null;
            }
            return subscription.getPassword().equals(password) ? subscription : null;
        } catch (SQLException e) {
            return null;
        }
    }

    public Transaction unlockBike(int totemId, Subscription subscription, BikeType type) {
        try {

            if (!subscription.isAdmin()) {
                if (subscription.getType() == SubscriptionType.day
                        || subscription.getType() == SubscriptionType.week && !subscription.isAdmin()) {
                    subscription.activate();
                }
                subscriptions.update(subscription);
            }

            var totem = totems.queryForId(totemId);
            if (totem == null) {
                // Panic
            }
            var _grips = totem.getGrips();
            var maybeGrip = _grips.stream().filter(x -> x.getType() == type && x.getBike() != null).findFirst();
            if (!maybeGrip.isPresent()) {
                return null;
            }
            var grip = maybeGrip.get();
            grips.refresh(grip);
            var bike = grip.getBike();
            grip.setBike(null);;

            grips.update(grip);

            Rental rental = new Rental(totem, subscription, bike);
            rentals.create(rental);

            GripManager.getInstance().unlockGrip(grip);

            return new Transaction(grip, rental);
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean bikeRemoved(int gripId) {
        try {
            Grip grip = grips.queryForId(gripId);
            var bike = grip.getBike();

            bike.setGrip(null);
            grip.setBike(null);

            bikes.update(bike);
            grips.update(grip);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // @requires rental != null
    public boolean bikeNotRemoved(Rental rental) {
        try {
            rentals.delete(rental);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean returnBike(Grip grip, int bikeId) {
        try {

            Bike bike = bikes.queryForId(bikeId);

            var r = bike.getRentals().stream().filter(x -> x.getEnd() == null).findFirst();

            if (!r.isPresent()) {
                return false;
            }

            var rental = r.get();
            rentals.refresh(rental);

            var subscription = rental.getSubscription();

            subscriptions.refresh(subscription);

            Date start = rental.getStart();
            Date end = DateUtils.now();

            bike.setGrip(grip);

            grip.setBike(bike);

            rental.setEnd(end);

            rentals.update(rental);
            bikes.update(bike);
            grips.update(grip);

            var duration = DateUtils.sub(start, end);

            var amount = getPayAmount(subscription, bike, duration);

            var card = subscription.getCard();

            boolean paySuccess = CardManager.getInstance().pay(card, amount);

            return paySuccess;
        } catch (SQLException e) {
            return false;
        }
    }

    public Rental getLastRental(Subscription subscription) {
        try {
            subscriptions.refresh(subscription);
            var sorted = subscription.getRentals().stream().sorted(new Comparator<Rental>() {
                @Override
                public int compare(Rental o1, Rental o2) {
                    var end1 = o1.getEnd();
                    var end2 = o2.getEnd();

                    if (end1 == null) {
                        return -1;
                    } else if (end2 == null) {
                        return 1;
                    } else {
                        return end1.compareTo(end2);
                    }
                }

            });

            var first = sorted.findFirst();
            if (first.isPresent()) {
                return first.get();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public double getPayAmount(Subscription subscription, Bike bike, Duration duration) {
        var minutes = duration.toMinutes();
        var halfHours = minutes / 30 + (minutes % 30 > 0 ? 1 : 0);

        double toPay = 0;

        if (duration.toHours() >= 24) {
            toPay = 150;
        } else if (duration.toMinutes() > 120) {
            int exceeds = subscription.getNumberOfExceed();
            exceeds += 1;
            subscription.setNumberOfExceed(exceeds);
        }

        toPay += bike.getCost(halfHours);

        return toPay;
    }

    public boolean checkReturn(Subscription subscription) {
        return subscription.getRentals().stream().allMatch(x -> x.getEnd() != null);
    }

    public boolean reportBroken(int code) {
        try {
            var bike = bikes.queryForId(code);
            if (bike == null)
                return false;
            bike.setBroken(true);
            bikes.update(bike);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public int meanBikesUsed() {
        try {
            var r = rentals.queryForAll();
            var minDate = r.stream().min(new Comparator<Rental>() {
                @Override
                public int compare(Rental o1, Rental o2) {
                    return Long.compare(o1.getStart().getTime(), o2.getStart().getTime());
                }
            }).get().getStart();
            long days = DateUtils.sub(minDate, DateUtils.now()).toDays();
            return (int) (r.size() / days);
        } catch (Exception e) {
            return 0;
        }
    }

    public Totem mostUsedTotem() {
        try {
            var t = totems.queryForAll();
            var o = t.stream().max(new Comparator<Totem>() {
                public int compare(Totem o1, Totem o2) {
                    return Integer.compare(o1.getRentals().size(), o2.getRentals().size());
                };
            });
            if (o.isPresent()) {
                return o.get();
            } else {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    // @requires t != null
    // @requires t.getGrip() != null
    // @requires t.getRental() != null
    public boolean transactionCanceled(Transaction t) {
        try {
            Rental rental = t.getRental();
            Grip grip = t.getGrip();

            grips.refresh(grip);
            rentals.refresh(rental);

            if (grip.getBike() != null) {
                rentals.delete(rental);
            } else {
                return false;
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Totem nearestTotem(int fromId) {
        try {
            Totem totem = totems.queryForId(fromId);

            List<Totem> t = totems.queryForAll();

            var s = t.stream().filter(x -> x.getId() != fromId).min(new Comparator<Totem>() {
                public int compare(Totem o1, Totem o2) {
                    var v1 = Math.sqrt(Math.pow(Math.abs(o1.getX() - totem.getX()), 2)
                            + Math.pow(Math.abs(o1.getY() - totem.getY()), 2));
                    var v2 = Math.sqrt(Math.pow(Math.abs(o2.getX() - totem.getX()), 2)
                            + Math.pow(Math.abs(o2.getY() - totem.getY()), 2));
                    return Double.compare(v1, v2);
                }
            });

            return s.get();
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Totem> getTotems() {
        try {
            return totems.queryForAll();
        } catch (SQLException e) {
            return null;
        }
    }

    public Totem getTotem(int id) {
        try {
            return totems.queryForId(id);
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Grip> getGrips(Totem totem) {
        try {
            totems.update(totem);
            return totem.getGrips().stream().collect(Collectors.toList());
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Bike> getBikes(int totemId) {
        try {
            var totem = totems.queryForId(totemId);
            return getBikes(totem);
        } catch (SQLException e) {
            return null;
        }
    }

    // @requires totem != null
    public List<Bike> getBikes(Totem totem) {
        try {
            totems.refresh(totem);
            Collection<Grip> grips = totem.getGrips();
            List<Bike> b = grips.stream().map(g -> g.getBike()).filter(x -> x != null).collect(Collectors.toList());
            b.forEach(x -> {
                try {
                    bikes.refresh(x);
                } catch (Exception e) {
                }
            });
            return b;
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean addBike(Grip grip, BikeType type) {
        try {
            var bike = new Bike(type);
            bikes.create(bike);
            bike.setGrip(grip);
            grip.setBike(bike);
            grips.update(grip);
            bikes.update(bike);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean addGrip(Totem totem, BikeType type) {
        try {
            var g = totem.getGrips();
            var sorted = g.stream().sorted(new Comparator<Grip>() {
                public int compare(Grip o1, Grip o2) {
                    return Integer.compare(o1.getPosition(), o2.getPosition());
                };
            }).collect(Collectors.toList());
            int expectedPos = 0;
            for (int i = 0; i < sorted.size(); i++) {
                if (sorted.get(i).getPosition() != expectedPos)
                    break;
                expectedPos++;
            }
            grips.create(new Grip(totem, type, expectedPos));
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeGrip(Totem totem, Grip grip) {
        try {
            totems.update(totem);
            grips.delete(grip);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean removeBike(Bike bike) {
        try {
            var grip = bike.getGrip();
            bikes.delete(bike);
            if (grip != null) {
                grip.setBike(null);
                grips.update(grip);
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean makeBikeOperative(Bike bike) {
        try {
            bike.setBroken(false);
            bikes.update(bike);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public void createFakeData() {
        try {

            Totem t1 = new Totem("Via Adios 14", 0, 0);
            Totem t2 = new Totem("Via Vamos 12", 1, 2);

            totems.create(t1);
            totems.create(t2);

            Grip g11 = new Grip(t1, BikeType.standard, 0);
            Grip g12 = new Grip(t1, BikeType.electric, 1);
            Grip g13 = new Grip(t1, BikeType.electricBabySeat, 2);

            Grip g21 = new Grip(t2, BikeType.standard, 0);
            Grip g22 = new Grip(t2, BikeType.electric, 1);
            Grip g23 = new Grip(t2, BikeType.electricBabySeat, 2);

            Bike b1 = new Bike(BikeType.standard);
            Bike b2 = new Bike(BikeType.electric);
            Bike b3 = new Bike(BikeType.electricBabySeat);

            bikes.create(b1);
            bikes.create(b2);
            bikes.create(b3);

            grips.create(g11);
            grips.create(g12);
            grips.create(g13);
            grips.create(g21);
            grips.create(g22);
            grips.create(g23);

            g11.setBike(b1);
            g12.setBike(b2);
            g13.setBike(b3);

            b1.setGrip(g11);
            b2.setGrip(g12);
            b3.setGrip(g13);

            grips.update(g11);
            grips.update(g12);
            grips.update(g13);

            bikes.update(b1);
            bikes.update(b2);
            bikes.update(b3);

            Card card = new Card("1111111111111111", 444, DateUtils.oneYear());

            cards.create(card);

            Subscription admin = new Subscription("admin", SubscriptionType.admin, false);
            Subscription normal = new Subscription("normal", SubscriptionType.week, false);
            Subscription student = new Subscription("student", SubscriptionType.week, true);
            Subscription expired = new Subscription("expired", SubscriptionType.day, true);
            expired.setNumberOfExceed(3);

            admin.setCard(card);
            normal.setCard(card);
            student.setCard(card);
            expired.setCard(card);

            subscriptions.create(admin);
            subscriptions.create(normal);
            subscriptions.create(student);
            subscriptions.create(expired);
        } catch (SQLException e) {
            System.out.println("Error");
        }
    }
}
