package org.manuelelucchi.data;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Date;
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

            createFakeData();

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

    public Subscription register(String password, SubscriptionType type, boolean isStudent, int cardCode,
            Date cardExpireDate) {
        try {
            Subscription subscription = new Subscription(password, type, isStudent);
            Card card = new Card(cardCode, cardExpireDate);

            subscription.setCard(card);
            if (type == SubscriptionType.year) {
                subscription.activate();
            }

            subscriptions.create(subscription);
            cards.createIfNotExists(card);

            return subscription;
        } catch (SQLException e) {
            return null;
        }
    }

    public Subscription login(int code, String password) {
        try {
            var subscription = subscriptions.queryForId(code);
            // Check password
            return subscription;
        } catch (SQLException e) {
            return null;
        }
    }

    public Grip unlockBike(int totemId, Subscription subscription, BikeType type) {
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
            var grips = totem.getGrips();
            var maybeGrip = grips.stream().filter(x -> x.getType() == type && x.getBike() != null).findFirst();
            if (!maybeGrip.isPresent()) {
                return null;
            }
            var grip = maybeGrip.get();
            var bike = grip.getBike();

            Rental rental = new Rental(subscription, bike);
            rentals.create(rental);

            // Invia segnale a controllore di aprire la morsa

            return grip;
        } catch (SQLException e) {
            return null;
        }
    }

    public boolean bikeRemoved(Grip grip, Bike bike) {
        try {
            bike.setGrip(null);
            grip.setBike(null);

            bikes.update(bike);
            grips.update(grip);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean returnBike(Grip grip, Bike bike) {
        try {
            Rental rental = bike.getRentals().stream().filter(x -> x.getEnd() == null).findFirst().get();
            Subscription subscription = rental.getSubscription();

            Date start = rental.getStart();
            Date end = DateUtils.now();

            bike.setGrip(grip);

            grip.setBike(bike);

            rental.setEnd(end);

            rentals.update(rental);
            subscriptions.update(subscription);
            bikes.update(bike);
            grips.update(grip);

            var duration = DateUtils.sub(start, end);

            var amount = payAmount(subscription, bike, duration);

            var card = subscription.getCard();

            boolean paySuccess = card.pay(amount);

            return paySuccess;
        } catch (SQLException e) {
            return false;
        }
    }

    public double payAmount(Subscription subscription, Bike bike, Duration duration) {
        var minutes = duration.toMinutes();
        var halfHours = minutes / 30 + (minutes % 30 > 0 ? 1 : 0);

        double toPay = 0;

        if (duration.toHours() >= 24) {
            toPay = 150;
        } else if (duration.toMinutes() > 120) {
            int exceeds = subscription.getNumberOfExceed();
            exceeds += 1;
            if (exceeds == 3) {
                terminateSubscription(subscription);
            } else {
                subscription.setNumberOfExceed(exceeds);
            }
        }

        toPay += bike.getCost(halfHours);

        return toPay;
    }

    public void terminateSubscription(Subscription subscription) {

    }

    public boolean checkReturn(Subscription subscription) {
        return subscription.getRentals().stream().allMatch(x -> x.getEnd() != null);
    }

    public boolean setBroken(Bike bike) {
        try {
            bike.setBroken(true);
            bikes.update(bike);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public int numberOfBikesUsedPerDay() {
        return 0;
    }

    public Grip mostUsedRack() {
        return null;
    }

    public void createFakeData() throws SQLException {

        Totem t1 = new Totem("Via Adios 14");
        Totem t2 = new Totem("Via Vamos 12");

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

        Subscription admin = new Subscription("admin", SubscriptionType.admin, false);
        Subscription normal = new Subscription("normal", SubscriptionType.week, false);
        Subscription student = new Subscription("student", SubscriptionType.week, true);

        subscriptions.create(admin);
        subscriptions.create(normal);
        subscriptions.create(student);
    }
}
