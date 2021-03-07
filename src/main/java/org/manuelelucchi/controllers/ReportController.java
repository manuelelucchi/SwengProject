package org.manuelelucchi.controllers;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class ReportController extends Controller {
    @FXML
    public TextField numberField;

    @FXML
    public Label responseLabel;

    @FXML
    public void back() {
        navigate("HomeView");
    }

    @FXML
    public void report() {
        try {
            int code = Integer.parseInt(numberField.getText());
            boolean res = DbManager.getInstance().reportBroken(code);
            responseLabel.setText(res ? "Thanks, the bike is now blocked" : "Can't find the specified bike");
        } catch (NumberFormatException e) {
            Alert a = new Alert(AlertType.ERROR, "You have to insert a valid number");
            a.setTitle("Error");
            a.setHeaderText(null);
            a.showAndWait();
            numberField.setText("");
        }
    }

}
