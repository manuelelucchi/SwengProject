package org.manuelelucchi.controllers;

import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StatisticsController extends Controller {
    @FXML
    public Label meanBike;

    @FXML
    public Label maxTotem;

    @FXML
    public void back() {
        navigate("HomeView");
    }

    @Override
    public void init() {
        DbManager db = DbManager.getInstance();

        int meanBikeNumber = db.meanBikesUsed();
        meanBike.setText("Mean bikes used per day: " + meanBikeNumber);

        String mostUsedTotem = db.mostUsedTotem().getAddress();
        maxTotem.setText("Most used Totem: " + mostUsedTotem);
    }

}
