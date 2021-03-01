package org.manuelelucchi.controllers;

import java.io.IOException;

import org.manuelelucchi.App;
import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Subscription;

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
        Subscription subscription = db.login(Integer.parseInt(code), password);
        if (subscription == null) {

        } else {
            if (subscription.isAdmin()) {
                navigate("BikeView", subscription);
            } else if (subscription.isExpired()) {
                navigate("RegisterView", subscription);
            } else {
                navigate("BikeView", subscription);
            }
        }
    }
}
