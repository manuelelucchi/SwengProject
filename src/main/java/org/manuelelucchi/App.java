package org.manuelelucchi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static int totemId;

    public static int getTotemId() {
        return totemId;
    }

    @Override
    public void start(Stage stage) throws IOException {
        DbManager db = DbManager.getInstance();
        db.ensureCreated();
        // Se fallisce da gestire

        scene = new Scene(loadView("HomeView"), 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static Parent loadView(String view) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("views/" + view + ".fxml"));
        return loader.load();
    }

    public static Parent loadView(Controller sender, String view) throws IOException {
        return loadView(sender, view, null);
    }

    public static Parent loadView(Controller sender, String view, Object parameter) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("views/" + view + ".fxml"));
        Parent parent = loader.load();
        Controller controller = loader.getController();
        controller.onNavigateFrom(sender, parameter);
        return parent;
    }

    public static void navigate(Controller sender, String view, Object parameter) throws IOException {
        var parent = loadView(sender, view, parameter);
        scene.setRoot(parent);
    }

    public static void navigate(Controller sender, String view) throws IOException {
        navigate(sender, view, null);
    }

    public static void main(String[] args) {
        launch();
    }

}