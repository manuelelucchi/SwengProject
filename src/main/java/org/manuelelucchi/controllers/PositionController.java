package org.manuelelucchi.controllers;

import java.util.Timer;
import java.util.TimerTask;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.models.Rental;
import org.manuelelucchi.models.Transaction;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PositionController extends Controller {
    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.transaction = (Transaction) parameter;
    }

    @Override
    public void init() {
        var grip = transaction.getGrip();

        codeLabel.setText("" + grip.getPosition());

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // Se non ha ritirato la bici bloccala e cancella rental
                navigate("HomeView");
            }
        }, 60000);
    }

    private Transaction transaction;

    @FXML
    public Label codeLabel;

    @FXML
    public void cancel() {
        var rental = transaction.getRental();
        var subscription = rental.getSubscription();

        // Cancella rental
        navigate("BikeView", subscription);
    }
}
