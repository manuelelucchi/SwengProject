package org.manuelelucchi.controllers;

import java.util.Timer;
import java.util.TimerTask;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.BikeType;
import org.manuelelucchi.models.Grip;
import org.manuelelucchi.models.Subscription;
import org.manuelelucchi.models.Transaction;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;

/**
 * BikeController
 */
public class BikeController extends Controller {
    private Subscription subscription;
    private DbManager db;

    @FXML
    public Label priceLabel;

    @FXML
    public RadioButton standardRadio;

    @FXML
    public RadioButton electricRadio;

    @FXML
    public RadioButton electricBabySeatRadio;

    @FXML
    public TextField gripField;

    public ToggleGroup group;

    private BikeType type;

    private boolean hasConfirmed;

    private boolean hasReturned;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.subscription = (Subscription) parameter;
        this.hasConfirmed = false;
        this.hasReturned = false;
    }

    @Override
    public void init() {
        this.db = DbManager.getInstance();

        group = new ToggleGroup();

        standardRadio.setToggleGroup(group);
        electricRadio.setToggleGroup(group);
        electricBabySeatRadio.setToggleGroup(group);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> arg0, Toggle arg1, Toggle arg2) {
                bikeTypeChanged();
            }
        });

        standardRadio.setSelected(true);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!hasConfirmed || !hasReturned) {
                    navigate("HomeView");
                }
            }
        }, 60000);
    }

    private BikeType getSelectedType() {
        var toggle = group.getSelectedToggle();
        if (toggle.equals(standardRadio)) {
            return BikeType.standard;
        } else if (toggle.equals(electricRadio)) {
            return BikeType.electric;
        } else if (toggle.equals(electricBabySeatRadio)) {
            return BikeType.electricBabySeat;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static String getPriceAfterTheFirstHalfHour(BikeType type, boolean isAdmin) {
        if (isAdmin)
            return "nothing";
        switch (type) {
        case standard:
            return "0.5€ per half hour after the first one";
        default:
            return "0.25€ the first half hour and doubling going on";
        }
    }

    public void bikeTypeChanged() {
        type = getSelectedType();
        var price = getPriceAfterTheFirstHalfHour(type, subscription.isAdmin());
        priceLabel.setText("You'll pay " + price);
    }

    @FXML
    public void confirm() {
        type = getSelectedType();
        Transaction t = db.unlockBike(getTotemId(), subscription, type);
        this.hasConfirmed = true;
        if (t != null) {
            navigate("PositionView", t);
        } else {
            navigate("AlternativeView", subscription);
        }
    }

    @FXML
    public void home() {
        navigate("HomeView");
    }

    @FXML
    public void bikeReturned() {
        try {
            int gripId = Integer.parseInt(gripField.getText());
            DbManager.getInstance().returnBike(gripId, subscription);
            Alert a = new Alert(AlertType.INFORMATION, "Bike successfully returned");
            a.setHeaderText(null);
            a.show();
            hasReturned = true;
            navigate("HomeView");
        } catch (NumberFormatException e) {
            Alert a = new Alert(AlertType.ERROR, "Insert a valid number");
            a.setHeaderText(null);
            a.show();
        } catch (IllegalArgumentException e) {

        }
    }
}