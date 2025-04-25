package com.employeemis.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class MainController extends Controller {
  @FXML private StackPane root;
  private ScreensManager manager;

  @FXML
  private void switchToDashboard() {
    manager.switchTo("dashboard");
  }

  public void initialize() {
    Platform.runLater(() -> {
      manager = new ScreensManager(getApplication(), root);
      switchToDashboard();
    });
  }
}