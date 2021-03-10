package org.manuelelucchi.controllers;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.models.Subscription;

import javafx.fxml.FXML;

public class AdminController extends Controller {

    private Subscription subscription;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.subscription = (Subscription) parameter;
    }

    @FXML
    public void back() {
        navigate("BikeView", subscription);
    }
}
