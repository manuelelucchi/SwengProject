package org.manuelelucchi.controllers;

import java.util.Comparator;

import org.manuelelucchi.common.AlertUtils;
import org.manuelelucchi.common.Controller;
import org.manuelelucchi.common.DateUtils;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Rental;
import org.manuelelucchi.models.Subscription;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController extends Controller {
    @FXML
    public TextField codeField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public void back() {
        navigate("HomeView");
    }

    @FXML
    public void login() {
        DbManager db = DbManager.getInstance();
        var code = codeField.getText();
        var password = passwordField.getText();
        Subscription subscription = db.login(Integer.parseInt(code), password);
        if (subscription == null) {
            AlertUtils.showError("You are not subscribed or wrong password");
        } else if (subscription.isExpired()) {
            AlertUtils.showError("Your subscription is expired or blocked");
            navigate("RegistrationView");
        } else {
            var r = subscription.getRentals().stream().max(new Comparator<Rental>() {
                @Override
                public int compare(Rental o1, Rental o2) {
                    var end1 = o1.getEnd();
                    var end2 = o2.getEnd();
                    return Long.compare(end1 == null ? 0 : end1.getTime(), end2 == null ? 0 : end2.getTime());
                }
            });

            if (!r.isPresent()) {
                navigate("BikeView", subscription);
            } else if (r.get().getEnd() == null) {
                AlertUtils.showError("You must return your bike before renting another");
            }

            else if (DateUtils.sub(r.get().getEnd(), DateUtils.now()).toMinutes() < 5) {
                AlertUtils.showError("5 minutes must pass between one rental and another");
            } else {
                navigate("BikeView", subscription);
            }
        }
    }
}
