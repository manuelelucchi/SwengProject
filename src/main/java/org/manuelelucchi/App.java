package org.manuelelucchi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static int totemId = 1;

    public static int getTotemId() {
        return totemId;
    }

    public static void setTotemId(int totemId) {
        App.totemId = totemId;
    }

    @Override
    public void start(Stage stage) throws IOException {
        DbManager db = DbManager.getInstance();
        File file = new File("./data.db");
        file.delete();

        db.ensureCreated();

        scene = new Scene(loadView("HomeView"), 1280, 720);
        navigate(null, "HomeView");
        stage.setScene(scene);
        stage.setTitle("Bike Sharing");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
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
        controller.init();
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