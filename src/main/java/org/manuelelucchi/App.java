package org.manuelelucchi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import org.manuelelucchi.data.DbManager;

/**
 * JavaFX App
 */
public class App extends Application {

    void test() {
        var db = DbManager.getInstance();

        if (db.ensureCreated()) {
            // db.cre();
        } else {
            System.out.println("Errore nella creazione di roba");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        var scene = new Scene(loadView("LoginView"), 640, 480);
        stage.setScene(scene);
        stage.show();

        test();
    }

    private Parent loadView(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("views/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}