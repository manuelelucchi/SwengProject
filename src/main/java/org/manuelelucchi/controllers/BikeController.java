package org.manuelelucchi.controllers;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.BikeType;
import org.manuelelucchi.models.Grip;
import org.manuelelucchi.models.Subscription;
import org.manuelelucchi.models.Transaction;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

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

    public ToggleGroup group;

    private BikeType type;

    private boolean hasConfirmed;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.subscription = (Subscription) parameter;
        this.hasConfirmed = false;
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

        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                if (!hasConfirmed) {
                    navigate("HomeView");
                }
            }
        }, 20000);
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

    public void bikeTypeChanged() {
        type = getSelectedType();
        priceLabel.setText("You'll pay " + "qualcosa" + " after the first half hour");
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
}