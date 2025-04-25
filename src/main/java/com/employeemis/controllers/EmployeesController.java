package com.employeemis.controllers;

import com.employeemis.models.Department;
import com.employeemis.models.Employee;
import com.employeemis.repositories.EmployeeRepository;
import com.employeemis.repositories.RepositoryUpdateConsumer;
import com.employeemis.utils.Alerts;

import com.employeemis.utils.Filters;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class EmployeesController extends Controller {
  @FXML private TableColumn<Employee<String>, Double> salaryColumn;
  @FXML private TableColumn<Employee<String>, Integer> experienceColumn;
  @FXML private TableColumn<Employee<String>, Double> performanceColumn;
  @FXML private TableView<Employee<String>> employeesTable;
  @FXML private ChoiceBox<Department<String>> salaryAvgByDepartmentSelector;

  // Filters
  @FXML private TextField filterByNameOrSalaryOrPerformanceField;
  @FXML private ChoiceBox<String> filterBySelector;
  @FXML private ChoiceBox<Department<String>> filterByDepartmentSelector;
  private String filterBy;

  private EmployeeRepository<String> repository() {
    return getApplication().getEmployeeRepository();
  }

  @FXML
  private void applyFilterBy() {
    try {
      List<Employee<String>> sortedList = new ArrayList<>();
      switch (filterBy) {
        case "Name" -> Filters.Employee
          .byName(repository().getAll(), filterByNameOrSalaryOrPerformanceField.getText().trim())
          .forEachRemaining(sortedList::add);
        case "Department" -> {
          if (Objects.isNull(filterByDepartmentSelector.getValue()))
            return;
          Filters.Employee.byDepartment(repository().getAll(), filterByDepartmentSelector.getValue().getName())
            .forEachRemaining(sortedList::add);
        } case "Salary range" -> {
          String[] salaryRange = filterByNameOrSalaryOrPerformanceField.getText()
            .trim().replace(" ", "").split("-");

          if (salaryRange.length < 2)
            return;

          double salaryFrom = Double.parseDouble(salaryRange[0]);
          double salaryTo = Double.parseDouble(salaryRange[1]);

          if (salaryFrom > salaryTo)
            throw new IllegalArgumentException("Salary start range must be less than or equal to end range");

          Filters.Employee
            .bySalaryRange(repository().getAll(), salaryFrom, salaryTo).forEachRemaining(sortedList::add);
        } case "Performance rate" -> {
          double performance = Double.parseDouble(filterByNameOrSalaryOrPerformanceField.getText().trim());
          Filters.Employee
            .withPerformanceGreaterThanOrEqualTo(repository().getAll(), performance).forEachRemaining(sortedList::add);
        } case "top5Paid" -> sortedList = repository().getTop5Paid();
        default -> sortedList = repository().getAll();
      }
      populateTable(sortedList);
    } catch (Exception e) {
      Alerts.display(Alert.AlertType.ERROR, "ERROR", null, e.getMessage());
    }
  }

  private void changeFilterBy(boolean deptVisible, boolean othersVisible, String placeholder) {
    filterByDepartmentSelector.setVisible(deptVisible);
    filterByDepartmentSelector.setValue(null);
    filterByNameOrSalaryOrPerformanceField.setVisible(othersVisible);
    filterByNameOrSalaryOrPerformanceField.setPromptText(placeholder);
  }
  
  private void readyFilters(List<Department<String>> departments) {
    filterBy = "All";

    changeFilterBy(false, false, "");
    filterByDepartmentSelector.getItems().setAll(departments);
    filterBySelector.getItems().setAll(new ArrayList<>(List.of("All", "Name", "Department", "Salary range", "Performance rate")));
    filterBySelector.setValue("All");
    filterBySelector.getSelectionModel().selectedItemProperty().addListener((l, p, by) -> {
      filterBy = by;
      switch (by) {
        case "Name" -> changeFilterBy(false, true, "name");
        case "Department" -> changeFilterBy(true, false, "");
        case "Salary range" -> changeFilterBy(false, true, "salary range (e.g: 500 - 600)");
        case "Performance rate" -> changeFilterBy(false, true, "performance rate");
        default -> changeFilterBy(false, false, "");
      }
    });
  }
  
  private void readySalaryAvgReportComputer(List<Department<String>> departments) {
    salaryAvgByDepartmentSelector.getItems().setAll(departments);
    salaryAvgByDepartmentSelector.getSelectionModel().selectedItemProperty().addListener(
      (listener, prev, current) -> {
        Alerts.display(
          Alert.AlertType.INFORMATION,
          "Salary Average Report",
          null,
          String.format(
            "Salary Average in %s department is $%f", current.getName(),
            repository().getSalaryAverageByDepartment(current.getName())
          ));
      });
  }

  private void onReady() {
    employeesTable.getColumns().add(getEmployeeVoidTableColumn());
    List<Department<String>> departments = getApplication().getDepartmentRepository().getAll();
    readySalaryAvgReportComputer(departments);
    readyFilters(departments);
    applyFilterBy();
  }

  private void populateTable(List<Employee<String>> employees) {
    employeesTable.setItems(FXCollections.observableArrayList(employees));
    employeesTable.refresh();
  }

  private TableColumn<Employee<String>, Void> getEmployeeVoidTableColumn() {
    TableColumn<Employee<String>, Void> actionsColumn = new TableColumn<>("Actions");

    actionsColumn.setCellFactory(col -> new TableCell<>() {
      private final Button editBtn = new Button("✏️");
      private final Button deleteBtn = new Button("🗑️");
      {
        editBtn.setTooltip(new Tooltip("Edit"));
        deleteBtn.setTooltip(new Tooltip("Delete"));

        editBtn.setOnAction(e -> {
          Employee<String> employee = getTableView().getItems().get(getIndex());
          showUpdateForm(employee, repository()::update);
        });

        deleteBtn.setOnAction(e -> {
          Employee<String> employee = getTableView().getItems().get(getIndex());
          Optional<ButtonType> yesOrNo = Alerts.display(
            Alert.AlertType.CONFIRMATION,
            String.format("Delete Employee ~ %s (#%s)", employee.getName(), employee.getId()),
            null, "Are you sure?");

          if (yesOrNo.isPresent() && yesOrNo.get() == ButtonType.OK) {
            repository().remove(employee.getId());
            getTableView().getItems().remove(employee);
          }
        });
      }
      private final HBox hbox = new HBox(5, editBtn, deleteBtn);

      @Override
      protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : hbox);
      }
    });
    return actionsColumn;
  }

  private EmployeeFormController loadCreateUpdateForm() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/employeemis/employee-form.fxml"));
    Parent root = loader.load();
    EmployeeFormController controller = loader.getController();
    Stage popup = new Stage();

    controller.setStage(popup);
    controller.populateDepartments(getApplication().getDepartmentRepository().getAll());
    popup.initModality(Modality.APPLICATION_MODAL);
    popup.initOwner(getPrimaryStage());
    popup.setScene(new Scene(root, 320, 480));

    return controller;
  }

  private void showUpdateForm(Employee<String> record, RepositoryUpdateConsumer<String, String, Object> onUpdate) {
    try {
      EmployeeFormController controller = loadCreateUpdateForm();

      controller.setRecord(record);
      controller.setOnUpdate(onUpdate, e -> applyFilterBy());
      controller.getStage().setTitle("Update Employee ~ " + record.getId());
      controller.getStage().showAndWait();
    } catch (IOException e) {
      //noinspection CallToPrintStackTrace
      e.printStackTrace();
    }
  }

  private void showCreateForm(Consumer<Employee<String>> onCreate) {
    try {
      EmployeeFormController controller = loadCreateUpdateForm();

      controller.setOnCreate(onCreate);
      controller.getStage().setTitle("New Employee");
      controller.getStage().showAndWait();
    } catch (IOException e) {
      //noinspection CallToPrintStackTrace
      e.printStackTrace();
    }
  }

  @FXML
  private void handleOnCreate() {
    showCreateForm(e -> {
      repository().add(e);
      applyFilterBy();
    });
  }

  @FXML
  private void queryTop5Paid() {
    filterBy = "top5Paid";
    applyFilterBy();
  }

  public void initialize() {
    Platform.runLater(this::onReady);
  }
}
