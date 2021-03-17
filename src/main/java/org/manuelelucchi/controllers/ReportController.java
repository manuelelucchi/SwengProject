package org.manuelelucchi.controllers;

import java.util.stream.Collectors;

import org.manuelelucchi.common.AlertUtils;
import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Bike;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class ReportController extends Controller {
    @FXML
    public ChoiceBox<Bike> bikesBox;

    @Override
    public void init() {
        bikesBox.setConverter(new StringConverter<Bike>() {
            @Override
            public String toString(Bike b) {
                if (b != null) {
                    return b.getId() + "";
                }
                return "ERRORE";
            }

            @Override
            public Bike fromString(String arg0) {
                throw new IllegalAccessError();
            }
        });

        updateBikes();
    }

    public void updateBikes() {
        var bikes = DbManager.getInstance().getBikes(getTotemId()).stream().filter(x -> !x.isBroken())
                .collect(Collectors.toList());
        bikesBox.getItems().clear();
        bikesBox.getItems().addAll(bikes);
        bikesBox.getSelectionModel().selectFirst();
    }

    @FXML
    public void back() {
        navigate("HomeView");
    }

    @FXML
    public void report() {
        int code = bikesBox.getSelectionModel().getSelectedItem().getId();
        if (DbManager.getInstance().reportBroken(code)) {
            AlertUtils.showInfo("Done!");
            updateBikes();
        } else {
            AlertUtils.showError("");
        }
    }

}
