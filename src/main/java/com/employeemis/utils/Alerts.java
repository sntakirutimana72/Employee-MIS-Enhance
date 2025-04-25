package com.employeemis.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts {
  public static Optional<ButtonType> display(Alert.AlertType level, String title, String header, String message) {
    Alert alert = new Alert(level);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(message);

    return alert.showAndWait();
  }
}
