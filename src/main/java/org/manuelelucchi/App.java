package org.manuelelucchi;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.*;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.domain.User;

/**
 * JavaFX App
 */
public class App extends Application {

    void test() {
        var db = DbManager.getInstance();

        if (db.ensureCreated()) {
            db.test();
        } else {
            System.out.println("Errore nella creazione di roba");
        }

        // // this uses h2 but you can change it to match your database
        // String databaseUrl = "jdbc:sqlite:test.db";
        // // create a connection source to our database
        // ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl);

        // // instantiate the DAO to handle Account with String id
        // Dao<User, String> accountDao = DaoManager.createDao(connectionSource,
        // User.class);

        // // if you need to create the 'accounts' table make this call
        // TableUtils.createTable(connectionSource, User.class);

        // User account = new User("John", "Smith", "password", true);

        // // persist the account object to the database
        // accountDao.create(account);

        // // retrieve the account
        // List<User> users = accountDao.queryForAll();
        // // show its password
        // System.out.println("Query Size: " + users.size());

        // // close the connection source
        // connectionSource.close();
    }

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        test();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}