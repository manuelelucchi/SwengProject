package org.manuelelucchi.controllers;

import java.io.IOException;

import org.manuelelucchi.App;
import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Subscription;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class LoginController extends Controller {
    @FXML
    public TextField codeField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public void back() {
        navigate("HomeView");
    }

    @FXML
    public void login() {
        DbManager db = DbManager.getInstance();
        var code = codeField.getText();
        var password = passwordField.getText();
        Subscription subscription = db.login(Integer.parseInt(code), password);
        if (subscription == null) {
            Alert a = new Alert(AlertType.ERROR, "You are not subscribed or wrong password");
            a.setHeaderText(null);
            a.setTitle("Error");
            a.showAndWait();
            navigate("RegistrationView");
        } else if (subscription.isExpired()) {
            Alert a = new Alert(AlertType.ERROR, "Your subscription is expired");
            a.setHeaderText(null);
            a.setTitle("Error");
            a.showAndWait();
            navigate("RegistrationView");
        } else {
            navigate("BikeView", subscription);
        }
    }
}
