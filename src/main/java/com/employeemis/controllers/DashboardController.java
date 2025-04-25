package com.employeemis.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class DashboardController extends Controller {
  @FXML private StackPane container;
  private ScreensManager manager;

  @FXML
  private void switchToEmployee() {
    manager.switchTo("employees");
  }

  public void initialize() {
    Platform.runLater(() -> {
      manager = new ScreensManager(getApplication(), container);
      switchToEmployee();
    });
  }
}
