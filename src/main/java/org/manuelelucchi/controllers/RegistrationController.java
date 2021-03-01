package org.manuelelucchi.controllers;

import java.io.IOException;

import org.manuelelucchi.App;
import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

public class RegistrationController extends Controller {
    @FXML
    public PasswordField passwordField;

    @FXML
    public Label codeLabel;

    @FXML
    public Button nextButton;

    @FXML
    public Button registerButton;

    private User currentUser;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        nextButton.setVisible(false);
    }

    @FXML
    public void back() {
        navigate("HomeView");
    }

    // STUDENT DA GESTIRE
    @FXML
    public void register() {
        DbManager db = DbManager.getInstance();
        var password = passwordField.getText();
        var isStudent = false;
        var isAdmin = false;
        User user = db.register(password, isStudent, isAdmin);
        if (user != null) {
            codeLabel.setText("Your code: " + user.getCode());
            registerButton.setDisable(true);
            nextButton.setVisible(true);
            currentUser = user;
        } else {
            // Errore
        }
    }

    @FXML
    public void next() {
        navigate("SubscriptionView", currentUser);
    }
}
