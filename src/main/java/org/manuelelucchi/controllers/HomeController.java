package org.manuelelucchi.controllers;

import java.io.IOException;

import org.manuelelucchi.App;

import javafx.fxml.FXML;

/**
 * HomeController
 */
public class HomeController {
    @FXML
    public void login() throws IOException {
        App.navigate("LoginView");
    }

    @FXML
    public void register() throws IOException {
        App.navigate("RegistrationView");
    }
}