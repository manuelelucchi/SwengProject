package org.manuelelucchi.controllers;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Subscription;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AlternativeController extends Controller {
    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.subscription = (Subscription) parameter;
    }

    @Override
    public void init() {
        var totem = DbManager.getInstance().nearestTotem(getTotemId());
        if (totem != null) {
            totemLabel.setText(totem.getAddress());
        } else {
            totemLabel.setText("Error");
        }
    }

    @FXML
    public Label totemLabel;

    private Subscription subscription;

    @FXML
    public void change() {
        navigate("BikeView", subscription);
    }

    @FXML
    public void home() {
        navigate("HomeView");
    }
}
