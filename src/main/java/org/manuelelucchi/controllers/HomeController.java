package org.manuelelucchi.controllers;

import java.util.stream.Collectors;

import org.manuelelucchi.common.AlertUtils;
import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.data.GripManager;
import org.manuelelucchi.models.Grip;
import org.manuelelucchi.models.Totem;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 * HomeController
 */
public class HomeController extends Controller {

    @Override
    public void init() {
        var totems = DbManager.getInstance().getTotems();
        totemChoiceBox.getItems().addAll(totems);
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

        gripsBox.setConverter(new StringConverter<Grip>() {
            @Override
            public String toString(Grip arg0) {
                if (arg0 == null) {
                    return "There aren't empty grips";
                }
                return arg0.getPosition() + "";
            }

            @Override
            public Grip fromString(String arg0) {
                throw new IllegalAccessError();
            }
        });

        updateGrips();
    }

    @FXML
    public ChoiceBox<Totem> totemChoiceBox;

    @FXML
    public ChoiceBox<Grip> gripsBox;

    @FXML
    public TextField bikeField;

    @FXML
    public void totemChanged() {
        setTotemId(totemChoiceBox.getSelectionModel().getSelectedItem().getId());
        updateGrips();
    }

    private void updateGrips() {
        gripsBox.getItems().clear();
        var grips = DbManager.getInstance().getGrips(totemChoiceBox.getValue()).stream()
                .filter(x -> x.getBike() == null).collect(Collectors.toList());
        gripsBox.getItems().addAll(grips);
        gripsBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void login() {
        navigate("LoginView");
    }

    @FXML
    public void register() {
        navigate("RegistrationView");
    }

    @FXML
    public void report() {
        navigate("ReportView");
    }

    @FXML
    public void statistics() {
        navigate("StatisticsView");
    }

    @FXML
    public void bikeReturned() {
        if (gripsBox.getValue() == null) {
            AlertUtils.showError("You can't return a bike if there are no free grips");
            return;
        }
        try {
            int bikeId = Integer.parseInt(bikeField.getText());
            if (DbManager.getInstance().returnBike(gripsBox.getValue(), bikeId)) {
                GripManager.getInstance().blockGrip(gripsBox.getValue());
                AlertUtils.showInfo("Bike successfully returned");
            } else {
                AlertUtils.showError("Error you inserted an invalid bike");
            }
        } catch (NumberFormatException e) {
            AlertUtils.showError("Insert a valid number");
        } catch (IllegalArgumentException e) {
            AlertUtils.showError("Error");
        }
    }
}