package org.manuelelucchi.data;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.manuelelucchi.common.DateUtils;
import org.manuelelucchi.models.*;

public class DbManager {
    private static DbManager _instance;

    private static String databaseUrl = "jdbc:sqlite:data.db";
    private ConnectionSource source;

    private Dao<User, String> users;
    private Dao<Bike, String> bikes;
    private Dao<Card, String> cards;
    private Dao<Subscription, String> subscriptions;
    private Dao<Totem, String> racks;
    private Dao<Grip, String> grips;
    private Dao<Rental, String> rentals;

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
            TableUtils.createTableIfNotExists(source, User.class);
            TableUtils.createTableIfNotExists(source, Grip.class);
            TableUtils.createTableIfNotExists(source, Rental.class);

            cards = DaoManager.createDao(source, Card.class);
            subscriptions = DaoManager.createDao(source, Subscription.class);
            bikes = DaoManager.createDao(source, Bike.class);
            users = DaoManager.createDao(source, User.class);
            grips = DaoManager.createDao(source, Grip.class);
            rentals = DaoManager.createDao(source, Rental.class);

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

    public User register(String password, boolean isStudent, boolean isAdmin) {
        try {
            var user = new User(password, isStudent, isAdmin);
            users.create(user);
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    public User login(int code, String password) {
        try {
            var user = users.queryForId(Integer.toString(code));
            return user;
        } catch (SQLException e) {
            return null;
        }
    }

    // Valutare transazioni
    public Subscription createSubscription(User user, SubscriptionType type, int cardCode, Date cardExpireDate) {
        try {
            // Check credit card

            Card card = new Card(cardCode, cardExpireDate);

            var subscription = new Subscription(user, card, type);
            card.getSubscriptions().add(subscription);
            user.setSubscription(subscription);

            if (type == SubscriptionType.year) {
                subscription.activate();
            }

            subscriptions.create(subscription);
            users.update(user);

            return subscription;
        } catch (SQLException e) {
            return null;
        }
    }

    public Grip unlockBike(Totem totem, User user, BikeType type) {
        try {
            Subscription sub = user.getSubscription();
            if (sub.getType() == SubscriptionType.day || sub.getType() == SubscriptionType.week && !user.isAdmin()) {
                sub.activate();
            }
            var grips = totem.getGrips();
            var maybeGrip = grips.stream().filter(x -> x.getType() == type && x.getBike() != null).findFirst();
            if (!maybeGrip.isPresent()) {
                return null;
            }
            var grip = maybeGrip.get();
            var bike = grip.getBike();

            Rental rental = new Rental(user, bike);
            rentals.create(rental);
            subscriptions.update(sub);

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
            User user = rental.getUser();

            Date start = rental.getStart();
            Date end = DateUtils.now();

            bike.setGrip(grip);

            grip.setBike(bike);

            rental.setEnd(end);

            rentals.update(rental);
            users.update(user);
            bikes.update(bike);
            grips.update(grip);

            var duration = DateUtils.sub(start, end);

            var amount = payAmount(user, bike, duration);

            Subscription subscription = user.getSubscription();

            var card = subscription.getCard();

            boolean paySuccess = card.pay(amount);

            return paySuccess;
        } catch (SQLException e) {
            return false;
        }
    }

    public double payAmount(User user, Bike bike, Duration duration) {
        var subscription = user.getSubscription();

        var minutes = duration.toMinutes();
        var halfHours = minutes / 30 + (minutes % 30 > 0 ? 1 : 0);

        double toPay = 0;

        if (duration.toHours() >= 24) {
            toPay = 150;
        } else if (duration.toMinutes() > 120) {
            int exceeds = subscription.getNumberOfExceed();
            exceeds += 1;
            if (exceeds == 3) {
                terminateSubscription(user, subscription);
            } else {
                subscription.setNumberOfExceed(exceeds);
            }
        }

        toPay += bike.getCost(halfHours);

        return toPay;
    }

    public void terminateSubscription(User user, Subscription subscription) {

    }

    public boolean checkReturn(User user) {
        return user.getRentals().stream().allMatch(x -> x.getEnd() != null);
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
}
