package org.manuelelucchi.controllers;

import java.io.IOException;

import org.manuelelucchi.App;
import org.manuelelucchi.common.Controller;

import javafx.fxml.FXML;

/**
 * HomeController
 */
public class HomeController extends Controller {
    @FXML
    public void login() {
        navigate("LoginView");
    }

    @FXML
    public void register() {
        navigate("RegistrationView");
    }
}