package org.manuelelucchi.controllers;

import java.io.IOException;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.common.DateUtils;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Subscription;
import org.manuelelucchi.models.SubscriptionType;
import org.manuelelucchi.models.User;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SubscriptionController extends Controller {
    private User user;

    @FXML
    public Button dayButton;

    @FXML
    public Button weekButton;

    @FXML
    public Button yearButton;

    @FXML
    public void day() {
        subscribe(SubscriptionType.day);
    }

    @FXML
    public void week() {
        subscribe(SubscriptionType.week);
    }

    @FXML
    public void year() {
        subscribe(SubscriptionType.year);
    }

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        user = (User) parameter;
    }

    private void subscribe(SubscriptionType type) {
        // Paga
        var cardCode = 0;
        var cardExpireDate = DateUtils.now();
        Subscription sub = DbManager.getInstance().createSubscription(user, type, cardCode, cardExpireDate);
        if (sub != null) {
            navigate("BikeView", user);
        }
    }

}
