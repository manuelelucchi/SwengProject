package org.manuelelucchi.data;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.manuelelucchi.domain.*;

public class DbManager {
    private static DbManager _instance;

    private static String databaseUrl = "jdbc:sqlite:data.db";
    private ConnectionSource source;

    private Dao<User, String> users;
    private Dao<Bike, String> bikes;
    private Dao<Card, String> cards;
    private Dao<Subscription, String> subscriptions;
    private Dao<Rack, String> racks;

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
            TableUtils.createTableIfNotExists(source, Rack.class);

            cards = DaoManager.createDao(source, Card.class);
            subscriptions = DaoManager.createDao(source, Subscription.class);
            bikes = DaoManager.createDao(source, Bike.class);
            users = DaoManager.createDao(source, User.class);
            racks = DaoManager.createDao(source, Rack.class);

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

    public void test() {
        try {
            var user = new User("Davide", "Orfei", "******", false);
            users.create(user);
            var subscription = Subscription.DaySubscription(user);
            subscriptions.create(subscription);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
