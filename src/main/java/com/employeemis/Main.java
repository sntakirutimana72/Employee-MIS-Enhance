package com.employeemis;

import com.employeemis.controllers.MainController;

import com.employeemis.models.Department;
import com.employeemis.models.Employee;
import com.employeemis.repositories.DepartmentRepository;
import com.employeemis.repositories.EmployeeRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
  private Stage primaryStage;
  private DepartmentRepository<String> departmentRepository;
  private EmployeeRepository<String> employeeRepository;

  private void configureStoragesOnStartup() {
    departmentRepository = new DepartmentRepository<>();
    employeeRepository = new EmployeeRepository<>();

    // Add department dummy state data
    departmentRepository.add(new Department<>("hr"));
    departmentRepository.add(new Department<>("it"));
    departmentRepository.add(new Department<>("customer care"));

    // Add employee dummy state data
    employeeRepository.add(new Employee<>(
      "1", "joe", departmentRepository.get(1), 2.4, 3, 4.1));
    employeeRepository.add(new Employee<>(
      "2", "jean", departmentRepository.get(2), 75.3, 3, 3.1));
  }

  public EmployeeRepository<String> getEmployeeRepository() {
    return employeeRepository;
  }

  public DepartmentRepository<String> getDepartmentRepository() {
    return departmentRepository;
  }

  public Stage getPrimaryStage() {
    return primaryStage;
  }

  @Override
  public void start(Stage stage) throws IOException {
    configureStoragesOnStartup();
    this.primaryStage = stage;
    // Load main layout from fxml
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
    // Create main layout instance
    Scene rootScene = new Scene(fxmlLoader.load(), 800, 480);
    MainController controller = fxmlLoader.getController();

    // Pass down primary stage to controller
    controller.setApplication(this);
    // Load global css styles
    rootScene.getStylesheets()
      .add(Objects.requireNonNull(getClass().getResource("/css/global.css")).toExternalForm());
    // Set main app window title
    stage.setTitle("Employee Management System");
    // Assign main layout to applet window
    stage.setScene(rootScene);
    // Display app
    stage.show();
  }

  public static void main(String[] args) {
        launch();
    }
}