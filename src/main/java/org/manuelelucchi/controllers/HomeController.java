package org.manuelelucchi.controllers;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Totem;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
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
    }

    @FXML
    public ChoiceBox<Totem> totemChoiceBox;

    @FXML
    public void totemChanged() {
        setTotemId(totemChoiceBox.getSelectionModel().getSelectedItem().getId());
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
}