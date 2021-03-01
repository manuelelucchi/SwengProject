package org.manuelelucchi.controllers;

import java.io.IOException;

import org.manuelelucchi.App;
import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.User;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
        User user = db.login(Integer.parseInt(code), password);
        if (user == null) {

        } else {
            var subscription = user.getSubscription();
            if (subscription == null || subscription.isExpired()) {
                navigate("SubscriptionView", user);
            } else {
                navigate("BikeView", user);
            }
        }
    }
}
