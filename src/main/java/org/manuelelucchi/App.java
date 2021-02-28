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

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        DbManager db = DbManager.getInstance();
        db.ensureCreated();
        // Se fallisce da gestire

        scene = new Scene(loadView("HomeView"), 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void navigate(String view) throws IOException {
        scene.setRoot(loadView(view));
    }

    private static Parent loadView(String view) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + view + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}