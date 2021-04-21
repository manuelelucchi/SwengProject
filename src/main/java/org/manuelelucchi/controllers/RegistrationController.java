package org.manuelelucchi.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.manuelelucchi.App;
import org.manuelelucchi.common.AlertUtils;
import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.models.Subscription;
import org.manuelelucchi.models.SubscriptionType;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

public class RegistrationController extends Controller {
    @FXML
    public PasswordField passwordField;

    @FXML
    public Button registerButton;

    @FXML
    public TextField codeField;

    @FXML
    public DatePicker expireDatePicker;

    @FXML
    public ChoiceBox<SubscriptionType> typeBox;

    @FXML
    public CheckBox studentBox;

    @FXML
    public TextField studentMailTextField;

    @FXML
    public TextField studentCodeTextField;

    @FXML
    public Label priceLabel;

    @FXML
    public void back() {
        navigate("HomeView");
    }

    @Override
    public void init() {
        typeBox.getItems().addAll(SubscriptionType.year, SubscriptionType.week, SubscriptionType.day);
        typeBox.setConverter(new StringConverter<SubscriptionType>() {
            @Override
            public String toString(SubscriptionType l) {
                switch (l) {
                case day:
                    return "Day (€4,50)";
                case week:
                    return "Week (€9,00)";
                case year:
                    return "Year (€36,00)";
                default:
                    return "";
                }
            }

            @Override
            public SubscriptionType fromString(String language) {
                throw new IllegalAccessError();
            }
        });

        expireDatePicker.setValue(LocalDate.now());

        typeBox.getSelectionModel().selectFirst();

        studentBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
                studentMailTextField.setVisible(studentBox.isSelected());
                studentCodeTextField.setVisible(studentBox.isSelected());
                updatePrice();
            }
        });
    }

    public void updatePrice() {
        var type = typeBox.getSelectionModel().getSelectedItem();
        double price = 0;
        if (studentBox.isSelected()) {
            price = 0;
        } else {
            switch (type) {
            case day:
                price = 4.5;
                break;
            case week:
                price = 9;
                break;
            case year:
                price = 36;
                break;
            }
        }
        priceLabel.setText("Current Total: " + price + "€");
    }

    @FXML
    public void typeBoxChanged() {
        updatePrice();
    }

    public boolean checkStudent() {
        var email = studentMailTextField.getText();
        int id;
        try {
            id = Integer.parseInt(studentCodeTextField.getText());
        } catch (Exception e) {
            return false;
        }
        return DbManager.getInstance().checkStudent(id, email);
    }

    @FXML
    public void register() {
        DbManager db = DbManager.getInstance();
        var password = passwordField.getText();
        var type = typeBox.getSelectionModel().getSelectedItem();
        var isStudent = studentBox.isSelected();
        if (isStudent && !checkStudent()) {
            AlertUtils.showError("Student data is invalid");
            return;
        }

        var cardCode = Integer.parseInt(codeField.getText());
        Date cardExpireDate = Date.from(expireDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Subscription subscription = db.register(password, type, isStudent, cardCode, cardExpireDate);
        if (subscription != null) {
            navigate("CodeView", subscription);
        } else {
            AlertUtils.showError("Error during registration");
        }
    }
}
