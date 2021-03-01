package org.manuelelucchi.controllers;

import org.manuelelucchi.common.Controller;

import javafx.fxml.FXML;

public class ReportController extends Controller {
    @FXML
    public void back() {
        navigate("HomeView");
    }

}
