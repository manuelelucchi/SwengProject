package org.manuelelucchi.controllers;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Date;

import org.manuelelucchi.App;
import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Subscription;
import org.manuelelucchi.models.SubscriptionType;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrationController extends Controller {
    @FXML
    public PasswordField passwordField;

    @FXML
    public Button registerButton;

    @FXML
    public TextField codeField;

    @FXML
    public DatePicker expireDatePicker;

    @FXML
    public CheckBox dayBox;

    @FXML
    public CheckBox weekBox;

    @FXML
    public CheckBox yearBox;

    @FXML
    public CheckBox studentBox;

    @FXML
    public void back() {
        navigate("HomeView");
    }

    @Override
    public void init() {
        dayBox.setSelected(true);

        dayBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                weekBox.setSelected(false);
                yearBox.setSelected(false);
                dayBox.setSelected(true);
            }
        });

        weekBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                dayBox.setSelected(false);
                yearBox.setSelected(false);
                weekBox.setSelected(true);
            }
        });

        yearBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                dayBox.setSelected(false);
                weekBox.setSelected(false);
                yearBox.setSelected(true);
            }
        });
    }

    public SubscriptionType getSubscriptionType() {
        var type = dayBox.isSelected() ? SubscriptionType.day : null;
        type = weekBox.isSelected() ? SubscriptionType.week : type;
        type = yearBox.isSelected() ? SubscriptionType.year : type;
        return type;
    }

    @FXML
    public void register() {
        DbManager db = DbManager.getInstance();
        var password = passwordField.getText();
        var type = getSubscriptionType();
        var isStudent = studentBox.isSelected();
        var cardCode = Integer.parseInt(codeField.getText());
        Date cardExpireDate = Date.from(expireDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Subscription subscription = db.register(password, type, isStudent, cardCode, cardExpireDate);
        if (subscription != null) {
            navigate("CodeView", subscription);
        } else {
            // Errore
        }
    }
}
