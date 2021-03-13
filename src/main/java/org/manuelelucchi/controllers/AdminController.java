package org.manuelelucchi.controllers;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.common.EnumUtils;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Bike;
import org.manuelelucchi.models.BikeType;
import org.manuelelucchi.models.Grip;
import org.manuelelucchi.models.Subscription;
import org.manuelelucchi.models.Totem;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class AdminController extends Controller {

    private Subscription subscription;

    private DbManager manager = DbManager.getInstance();

    @FXML
    public ChoiceBox<Totem> totemChoiceBox;

    @FXML
    public ChoiceBox<Grip> gripsChoiceBox;

    @FXML
    public ChoiceBox<BikeType> bikeTypeChoiceBox;

    @FXML
    public TextField bikeNameTextField;

    @FXML
    public ChoiceBox<Bike> bikesChoiceBox;

    @Override
    public void onNavigateFrom(Controller sender, Object parameter) {
        this.subscription = (Subscription) parameter;
    }

    @Override
    public void init() {
        totemChoiceBox.getItems().addAll(manager.getTotems());
        totemChoiceBox.setConverter(new StringConverter<Totem>() {
            @Override
            public String toString(Totem l) {
                return l.getAddress();
            }

            @Override
            public Totem fromString(String language) {
                throw new IllegalAccessError();
            }
        });
        totemChoiceBox.getSelectionModel().selectFirst();
        totemChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Totem>() {
            @Override
            public void changed(ObservableValue<? extends Totem> arg0, Totem arg1, Totem arg2) {
                updateGrips();
            }
        });

        gripsChoiceBox.setConverter(new StringConverter<Grip>() {
            @Override
            public String toString(Grip l) {
                return l.getPosition() + "";
            }

            @Override
            public Grip fromString(String language) {
                throw new IllegalAccessError();
            }
        });
        gripsChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Grip>() {
            @Override
            public void changed(ObservableValue<? extends Grip> arg0, Grip arg1, Grip arg2) {
                updateBikes();
            }
        });

        bikesChoiceBox.setConverter(new StringConverter<Bike>() {
            @Override
            public String toString(Bike l) {
                return l.getId() + ": " + EnumUtils.toString(l.getType());
            }

            @Override
            public Bike fromString(String language) {
                throw new IllegalAccessError();
            }
        });

        bikeTypeChoiceBox.getItems().addAll(BikeType.standard, BikeType.electric, BikeType.electricBabySeat);
        bikeTypeChoiceBox.setConverter(new StringConverter<BikeType>() {
            @Override
            public String toString(BikeType l) {
                return EnumUtils.toString(l);
            }

            @Override
            public BikeType fromString(String language) {
                throw new IllegalAccessError();
            }
        });
        bikeTypeChoiceBox.getSelectionModel().selectFirst();

        updateGrips();
    }

    private Grip getGrip() {
        return gripsChoiceBox.getSelectionModel().getSelectedItem();
    }

    private Totem getTotem() {
        return totemChoiceBox.getSelectionModel().getSelectedItem();
    }

    private void updateGrips() {
        var grips = manager.getGrips(getTotem());
        gripsChoiceBox.getItems().clear();
        gripsChoiceBox.getItems().addAll(grips);
        gripsChoiceBox.getSelectionModel().selectFirst();
        updateBikes();
    }

    private void updateBikes() {
        var bikes = manager.getBikes(getTotem());
        bikesChoiceBox.getItems().clear();
        bikesChoiceBox.getItems().addAll(bikes);
        bikesChoiceBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void removeGrip() {
        manager.removeGrip(getTotem(), getGrip());
        updateGrips();
    }

    @FXML
    public void addBike() {
        var type = bikeTypeChoiceBox.getSelectionModel().getSelectedItem();
        var grip = getGrip();
        manager.addBike(grip, type);
        updateBikes();
    }

    @FXML
    public void removeBike() {

    }

    @FXML
    public void bikeOperative() {

    }

    @FXML
    public void back() {
        navigate("BikeView", subscription);
    }
}
