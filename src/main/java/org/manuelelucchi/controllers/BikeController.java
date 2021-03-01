package org.manuelelucchi.controllers;

import java.io.IOException;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.BikeType;
import org.manuelelucchi.models.Grip;
import org.manuelelucchi.models.Subscription;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * BikeController
 */
public class BikeController extends Controller {
    private Subscription subscription;
    private DbManager db;

    @FXML
    public Button standardButton;

    @FXML
    public Button electricButton;

    @FXML
    public Button electricSeatButton;

    @FXML
    public Label bikePositionLabel;

    @FXML
    public Button okButton;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.subscription = (Subscription) parameter;
        this.db = DbManager.getInstance();
        this.okButton.setVisible(false);
    }

    @FXML
    public void standardBike() {
        disableAll();
        Grip grip = db.unlockBike(getTotemId(), subscription, BikeType.standard);
        execute(grip);
    }

    @FXML
    public void electricBike() {
        disableAll();
        Grip grip = db.unlockBike(getTotemId(), subscription, BikeType.electric);
        execute(grip);
    }

    @FXML
    public void electricSeatBike() {
        disableAll();
        Grip grip = db.unlockBike(getTotemId(), subscription, BikeType.electricBabySeat);
        execute(grip);
    }

    @FXML
    public void ok() {
        navigate("HomeView");
    }

    public void enableAll() {
        standardButton.setDisable(false);
        electricButton.setDisable(false);
        electricSeatButton.setDisable(false);
    }

    public void disableAll() {
        standardButton.setDisable(true);
        electricButton.setDisable(true);
        electricSeatButton.setDisable(true);
    }

    private void execute(Grip grip) {
        if (grip != null) {
            bikePositionLabel.setText("La bicicletta si trova nella morsa: " + grip.getPosition());
            okButton.setVisible(true);
        } else {
            bikePositionLabel.setText("Errore: Riprovare per favore");
            enableAll();
        }
    }
}