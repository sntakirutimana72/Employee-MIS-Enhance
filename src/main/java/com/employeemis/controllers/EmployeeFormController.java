package com.employeemis.controllers;

import com.employeemis.models.Department;
import com.employeemis.models.Employee;

import com.employeemis.repositories.RepositoryUpdateConsumer;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static com.employeemis.utils.Common.*;

public class EmployeeFormController {
  @FXML private TextField nameField;
  @FXML private ChoiceBox<Department<String>> departmentSelector;
  @FXML private TextField salaryField;
  @FXML private TextField experienceField;
  @FXML private TextField performanceField;
  @FXML private Label errorDisplay;

  private Stage stage;
  private Employee<String> record;
  private RepositoryUpdateConsumer<String, String, Object> onUpdate;
  private Consumer<?> onAfterUpdate;
  private Consumer<Employee<String>> onCreate;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void populateDepartments(List<Department<String>> departments) {
    departmentSelector.getItems().setAll(departments);
  }

  public Stage getStage() {
    return stage;
  }

  public void setRecord(Employee<String> record) {
    this.record = record;
    nameField.setText(record.getName());
    departmentSelector.setValue(record.getDepartment());
    salaryField.setText(String.valueOf(record.getSalary()));
    experienceField.setText(String.valueOf(record.getYearsOfExperience()));
    performanceField.setText(String.valueOf(record.getPerformanceRate()));
  }

  public void setOnCreate(Consumer<Employee<String>> onCreate) {
    this.onCreate = onCreate;
  }

  public void setOnUpdate(RepositoryUpdateConsumer<String, String, Object> onUpdate, Consumer<?> onAfterUpdate) {
    this.onUpdate = onUpdate;
    this.onAfterUpdate = onAfterUpdate;
  }

  private <T> void handleUpdate(String attribute, T value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    if (!Objects.isNull(record)) {
      Method getter = hasGetter(record.getClass(), attribute);
      if (!getter.invoke(record).equals(value)) {
        this.onUpdate.accept(record.getId(), attribute, value);
      }
    }
  }

  @FXML
  private void handleSubmit() {
    try {
      String name = nameField.getText().trim();
      raiseIfIllegal(name.isBlank(), "Name is required");

      Department<String> department = departmentSelector.getValue();
      raiseIfIllegal(Objects.isNull(department), "Department is required");

      String salaryStr = salaryField.getText().trim();
      raiseIfIllegal(salaryStr.isBlank(), "Salary is required");
      double salary = Double.parseDouble(salaryStr);

      String expStr = experienceField.getText().trim();
      raiseIfIllegal(expStr.isBlank(), "Experience is required");
      int experience = Integer.parseInt(expStr);

      String performanceStr = performanceField.getText().trim();
      raiseIfIllegal(performanceStr.isBlank(), "Performance is required");
      double performance = Double.parseDouble(performanceStr);

      if (Objects.isNull(record)) {
        Employee<String> employee = new Employee<>(
          UUID.randomUUID().toString().replace("-", ""),
          name, department, salary, experience, performance);
        onCreate.accept(employee);
      } else {
        handleUpdate("name", name);
        handleUpdate("department", department);
        handleUpdate("salary", salary);
        handleUpdate("yearsOfExperience", experience);
        handleUpdate("performanceRate", performance);
      }

      stage.close();
    } catch (Exception e) {
      errorDisplay.setText(e.getMessage());
    }
    if (!Objects.isNull(onAfterUpdate))
      onAfterUpdate.accept(null);
  }

  @FXML
  private void handleCancel() {
    stage.close();
  }
}
