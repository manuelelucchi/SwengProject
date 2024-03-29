package org.manuelelucchi.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.manuelelucchi.App;
import org.manuelelucchi.common.AlertUtils;
import org.manuelelucchi.common.Controller;
import org.manuelelucchi.data.CardManager;
import org.manuelelucchi.data.DbManager;
import org.manuelelucchi.data.StudentManager;
import org.manuelelucchi.models.Subscription;
import org.manuelelucchi.models.SubscriptionType;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
    public TextField cvvField;

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
    public HBox studentContainer;

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
                studentContainer.setVisible(studentBox.isSelected());
                updatePrice();
            }
        });

        studentContainer.setVisible(false);
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

    static boolean isValidEmail(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
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
        if (!isValidEmail(email)) {
            return false;
        }
        return StudentManager.getInstance().isStudent(id, email);
    }

    @FXML
    public void register() {
        DbManager db = DbManager.getInstance();
        var password = passwordField.getText();
        var type = typeBox.getSelectionModel().getSelectedItem();
        var isStudent = studentBox.isSelected();

        if (password.length() < 4) {
            AlertUtils.showError("Password too short");
            return;
        }

        if (password.length() > 20) {
            AlertUtils.showError("Password too long");
            return;
        }

        String cardCode = codeField.getText();
        if (cardCode.length() != 16) {
            AlertUtils.showError("Card code is not valid");
            return;
        }

        for (var c : cardCode.toCharArray()) {
            if (!Character.isDigit(c)) {
                AlertUtils.showError("Card code is not valid");
                return;
            }
        }

        int cardCVV;
        try {
            cardCVV = Integer.parseInt(cvvField.getText());
            if (cardCVV > 999 || cardCVV < 100) {
                throw new Exception();
            }
        } catch (Exception e) {
            AlertUtils.showError("Card CVV is not valid");
            return;
        }

        Date cardExpireDate = Date.from(expireDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (!CardManager.getInstance().isValidCard(cardCode, cardExpireDate, cardCVV, type)) {
            AlertUtils.showError("Card not invalid or will expire before the end of the subscription");
            return;
        }

        if (isStudent && !checkStudent()) {
            AlertUtils.showError("Student data is invalid");
            return;
        }

        Subscription subscription = db.register(password, type, isStudent, cardCode, cardExpireDate, cardCVV);
        if (subscription != null) {
            navigate("CodeView", subscription);
        } else {
            AlertUtils.showError("Error during registration");
        }
    }
}
