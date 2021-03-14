package org.manuelelucchi.common;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtils {
    public static void showError(String text) {
        Alert a = new Alert(AlertType.ERROR, text);
        a.setHeaderText(null);
        a.show();
    }

    public static void showInfo(String text) {
        Alert a = new Alert(AlertType.INFORMATION, text);
        a.setHeaderText(null);
        a.show();
    }
}
