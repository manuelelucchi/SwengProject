package org.manuelelucchi.controllers;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
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

        codeLabel.setText("You can retire the bike number " + transaction.getRental().getBike().getId() + " at the grip number " + grip.getPosition());
    }

    private Transaction transaction;

    @FXML
    public Label codeLabel;

    @FXML
    public void cancel() {
        var rental = transaction.getRental();
        var subscription = rental.getSubscription();

        DbManager.getInstance().transactionCanceled(transaction);
        navigate("BikeView", subscription);
    }

    @FXML
    public void bikeRetired() {
        navigate("HomeView");
    }
}
