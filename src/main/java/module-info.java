module com.employeemis {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;

  requires org.controlsfx.controls;
  requires com.dlsc.formsfx;
  requires net.synedra.validatorfx;
  requires org.kordamp.ikonli.javafx;
  requires org.kordamp.bootstrapfx.core;

  opens com.employeemis to javafx.fxml;
  exports com.employeemis;
  exports com.employeemis.controllers;
  opens com.employeemis.controllers to javafx.fxml;
  exports com.employeemis.models;
  opens com.employeemis.models to javafx.fxml;
  exports com.employeemis.repositories;
  opens com.employeemis.repositories to javafx.fxml;
}