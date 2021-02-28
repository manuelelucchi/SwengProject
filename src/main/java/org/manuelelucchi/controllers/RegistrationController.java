package org.manuelelucchi.controllers;

import java.io.IOException;

import org.manuelelucchi.App;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.User;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationController {
    @FXML
    public PasswordField passwordField;

    @FXML
    public Label codeLabel;

    @FXML
    public void back() throws IOException {
        App.navigate("HomeView");
    }

    @FXML
    public void register() throws IOException {
        DbManager db = DbManager.getInstance();
        var password = passwordField.getText();
        var isStudent = false;
        var isAdmin = false;
        User user = db.register(password, isStudent, isAdmin);
        codeLabel.setText("Your code: " + user.getCode());
    }
}
