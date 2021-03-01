package org.manuelelucchi.controllers;

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

    @FXML
    public void report() {
        navigate("ReportView");
    }

    @FXML
    public void statistics() {
        navigate("StatisticsView");
    }
}