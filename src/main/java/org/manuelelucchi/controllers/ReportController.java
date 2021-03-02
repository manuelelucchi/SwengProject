package org.manuelelucchi.controllers;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
        int code = Integer.parseInt(numberField.getText());
        boolean res = DbManager.getInstance().reportBroken(code);
        responseLabel.setText(res ? "Thanks, the bike is now blocked" : "Can't find the specified bike");
    }

}
