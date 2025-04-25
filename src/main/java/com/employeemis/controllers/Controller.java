package com.employeemis.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class Controller {
  @FXML
  private com.employeemis.Main application;

  public com.employeemis.Main getApplication() {
    return application;
  }

  public void setApplication(com.employeemis.Main application) {
    this.application = application;
  }

  public Stage getPrimaryStage() {
    return getApplication().getPrimaryStage();
  }
}
