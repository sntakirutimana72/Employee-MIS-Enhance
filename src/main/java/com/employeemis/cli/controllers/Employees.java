package com.employeemis.cli.controllers;

import com.employeemis.cli.Helpers;
import com.employeemis.cli.Main;
import com.employeemis.models.Department;
import com.employeemis.models.Employee;
import com.employeemis.repositories.DepartmentRepository;
import com.employeemis.repositories.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Employees extends Controller {
  private List<Employee<Integer>> listOfEmployees;
  
  public Employees(Main app) {
    super(app);
    Helpers.Printer.alert("Employees Menu");
    
    listOfEmployees = new ArrayList<>();
  }
  
  @Override
  public EmployeeRepository<Integer> getRepository() {
    return getApp().getEmployeeRepository();
  }

  private DepartmentRepository<Integer> getDepartmentRepository() {
    return getApp().getDepartmentRepository();
  }

  private void previewTable(String title) {
    List<String> columns = new ArrayList<>(List.of("ID", "Name", "Department", "Salary", "Years of Experience", "Performance Rate"));
    Function<Employee<Integer>, List<String>> getRow = (e) -> new ArrayList<>(List.of(
      String.valueOf(e.getId()),
      e.getName(),
      e.getDepartment().getName(),
      String.valueOf(e.getSalary()),
      String.valueOf(e.getYearsOfExperience()),
      String.valueOf(e.getPerformanceRate())
    ));
    Helpers.Printer.tabular(title, columns, listOfEmployees.stream().map(getRow).toList());
  }

  private int createDepartment() throws Helpers.Errors.AbortException {
    while (true) {
      try {
        String name = Helpers.Prompt.getText(getScanner(), "Enter department name:\n> ");
        Department<Integer> department = new Department<>(name);
        getDepartmentRepository().add(department);
        return department.getId();
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  private void processList(int choice) {
    listOfEmployees = choice == 0 ?
      getRepository().getAll() :
      getRepository().getTop5Paid();
    previewTable(choice == 0 ? "List of All Employees" : "List of Top 5 Paid Employees");
  }

  private void processCreate() throws Helpers.Errors.AbortException {
    Helpers.Printer.alert("Create New Employee");
    while (true) {
      try {
        // Get employee ID
        int employeeId = Helpers.Prompt.getPositiveInt(
          getScanner(), "Enter employee ID (must be a number):\n> ", 1);
        // Get employee full name
        String fullName = Helpers.Prompt.getText(getScanner(), "Enter employee name(s):\n> ");
        // Select department
        List<Department<Integer>> listOfDepartments = getDepartmentRepository().getAll();
        int departmentSelection = listOfDepartments.isEmpty() ?
          createDepartment() :
          Helpers.Selectors.selectEntity("department", getScanner(), listOfDepartments);
        // Get employee salary
        double salary = Helpers.Prompt.getDouble(getScanner(), "Enter salary:\n> ");
        // Get employee years of experience
        int yearsOfExperience = Helpers.Prompt.getPositiveInt(getScanner(), "Enter years of experience:\n> ", 0);
        // Get employee ID
        double performance = Helpers.Prompt.getDouble(getScanner(), "Enter performance rate:\n> ");

        // Now, create employee record
        Employee<Integer> employee = new Employee<>(
          employeeId,
          fullName,
          getDepartmentRepository().get(departmentSelection),
          salary,
          yearsOfExperience,
          performance
        );
        getRepository().add(employee);
        Helpers.Printer.alert("Employee created successfully!!");
        return;
      } catch (IllegalArgumentException e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }
  }

  private void processUpdate() throws Helpers.Errors.AbortException {
    List<Employee<Integer>> employees = getRepository().getAll();
    Helpers.Errors.cannotBeEmpty("employee", employees.isEmpty());
    int empSelection = Helpers.Selectors.selectEntity("employee", getScanner(), employees);

    while (true) {
      try {
        List<String> fields = new ArrayList<>(
          List.of("name", "department", "salary", "yearsOfExperience", "performanceRate"));
        // Get field name to be updated
        int selectedAttrib = Helpers.Selectors.select(
          "Select field to be updated:", getScanner(), fields);
        // Query selected employee
        Employee<Integer> selectedEmployee = getRepository().get(empSelection);

        switch (fields.get(selectedAttrib)) {
          // This deals with department updates
          case "department" -> {
            List<Department<Integer>> listOfDepartments = getDepartmentRepository().getAll();
            Helpers.Errors.cannotBeEmpty("department", listOfDepartments.isEmpty());
            int deptSelection = Helpers.Selectors.selectEntity(
              "department: ", getScanner(), listOfDepartments);
            Department<Integer> selectedDepartment = getDepartmentRepository().get(deptSelection);
            // If selected department is the same as the current one, abort
            if (selectedDepartment.getId().equals(selectedEmployee.getDepartment().getId()))
              throw new IllegalArgumentException("You selected the same department");
            getRepository()
              .update(selectedEmployee.getId(), "department", selectedDepartment);
          }

          // This deals with name updates
          case "name" -> {
            String name = Helpers.Prompt.getText(getScanner(), "Enter value of `name`:\n> ");
            getRepository().update(selectedEmployee.getId(), "name", name);
          }

          // This block only deals with yearsOfExperience updates
          case "yearsOfExperience" -> {
            int yearsOfExperience = Helpers.Prompt
              .getPositiveInt(getScanner(), "Enter value of `yearsOfExperience`:\n> ", 0);
            getRepository()
              .update(selectedEmployee.getId(), "yearsOfExperience", yearsOfExperience);
          }

          // This section deals with performanceRate & salary updates
          default -> {
            double value = Helpers.Prompt
              .getDouble(getScanner(), String.format("Enter value of `%s`:\n> ", fields.get(selectedAttrib)));
            getRepository()
              .update(selectedEmployee.getId(), fields.get(selectedAttrib), value);
          }
        }
        Helpers.Printer.alert(String.format("Employee no~(%s) was successfully updated!!", empSelection));
        return;
      } catch (IllegalArgumentException e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }
  }

  private void processRemoval() throws Helpers.Errors.AbortException {
    List<Employee<Integer>> employees = getRepository().getAll();
    Helpers.Errors.cannotBeEmpty("employee", employees.isEmpty());

    int selection = Helpers.Selectors.selectEntity("employee", getScanner(), employees);

    getRepository().remove(selection);
    Helpers.Printer.alert(String.format("Employee no~(%s) was successfully deleted!!", selection));
  }

  private void processCRUD(int choice) throws Helpers.Errors.AbortException {
    switch (choice) {
      case 3 -> processCreate();
      case 4 -> processUpdate();
      default -> processRemoval();
    }
  }

  private void processShowSalaryAverage() throws Helpers.Errors.AbortException {
    List<Department<Integer>> departments = getDepartmentRepository().getAll();
    Helpers.Errors.cannotBeEmpty("department", departments.isEmpty());
    int choice = Helpers.Selectors.selectEntity("department", getScanner(), departments);
    String departmentName = getDepartmentRepository().get(choice).getName();
    double average = getRepository().getSalaryAverageByDepartment(departmentName);
    Helpers.Printer.alert(String.format("Salary Average in %s department is $%f", departmentName, average));
  }

  public void process() throws Helpers.Errors.AbortException {
    //noinspection InfiniteLoopStatement
    while (true) {
      try {
        int choice = Helpers.Selectors.select("Select option", getScanner(), new ArrayList<>(List.of(
          "List All Employees",
          "List Top 5 Paid Employees",
          "Show Employee Salary Average (By Department)",
          "Create Employee",
          "Update Employee",
          "Delete Employee"
        )));
        switch (choice) {
          case 0:
          case 1:
            processList(choice);
            break;
          case 2:
            processShowSalaryAverage();
            break;
          default:
            processCRUD(choice);
        }
      } catch (IllegalArgumentException e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }
  }
}
