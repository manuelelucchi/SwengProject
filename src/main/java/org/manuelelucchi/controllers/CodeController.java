package org.manuelelucchi.controllers;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.models.Subscription;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class CodeController extends Controller {

    @FXML
    public Label codeLabel;

    @FXML
    public Button nextButton;

    private Subscription subscription;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        subscription = (Subscription) parameter;
    }

    @Override
    public void init() {
        codeLabel.setText("" + subscription.getCode());
    }

    @FXML
    public void next() {
        navigate("BikeView", subscription);
    }

}
