package com.employeemis.cli.controller;

import com.employeemis.cli.Helpers;
import com.employeemis.cli.Main;
import com.employeemis.cli.Exceptions;
import com.employeemis.cli.processor.EmployeeProcessor;
import com.employeemis.models.Department;
import com.employeemis.repositories.DepartmentRepository;
import com.employeemis.repositories.EmployeeRepository;
import com.employeemis.utils.Validators;

import static com.employeemis.utils.Exceptions.ResourceNotFoundException;

import java.util.List;

public class EmployeeController extends Controller {
  
  public EmployeeController(Main cliApplication) {
    super(cliApplication);
    Helpers.Printer.alert("Employee Menu");
  }
  
  @Override
  public EmployeeRepository<Integer> repository() {
    return getCliApplication().getEmployeeRepository();
  }

  private DepartmentRepository<Integer> getDepartmentRepository() {
    return getCliApplication().getDepartmentRepository();
  }

  private void giveSalaryRaise() {
    while (true) {
      try {
        // Enter performance rate constraint
        double performanceRate = Helpers.Prompt.getDouble(getScanner(), "Enter performance rate (0-5):\n> ");
        Validators.Employee.validatePerformanceRate(performanceRate);

        // Enter raise percentage
        double percentage = Helpers.Prompt.getDouble(getScanner(), "Enter raise percentage (0 < X <= 100):\n> ");
        if (percentage <= 0 || percentage > 100)
          throw new IllegalArgumentException("Raise percentage must be `0 < X <= 100`");

        // Apply raise
        repository().giveSalaryRaise(performanceRate, percentage);
        Helpers.Printer.alert("Salary raise was successful!!");
        return;
      } catch (IllegalArgumentException e) {
        Helpers.Printer.alert(e.getMessage());
      } catch (Exceptions.AbortException e) {
        Helpers.Printer.alert("Navigating back..");
        return;
      }
    }
  }

  private void showSalaryAverage() {
    try {
      List<Department<Integer>> departments = Helpers.Policies.cannotBeEmpty("department", getDepartmentRepository()::getAll);
      int choice = Helpers.Selectors.selectEntity("department", getScanner(), departments);
      String departmentName = getDepartmentRepository().get(choice).getName();
      double average = repository().getSalaryAverageByDepartment(departmentName);
      Helpers.Printer.alert(String.format("Salary Average in %s department is $%f", departmentName, average));
    } catch (ResourceNotFoundException | Exceptions.AbortException e) {
      Helpers.Printer.alert(e.getMessage());
    }
  }

  public void process() throws Exceptions.AbortException {
    //noinspection InfiniteLoopStatement
    while (true) {
      try {
        int choice = Helpers.Selectors.select("Select option", getScanner(), List.of(
          "List Employees",
          "Show Employee Salary Average (By Department)",
          "Give salary raise",
          "Create Employee",
          "Update Employee",
          "Delete Employee"
        ));

        switch (choice) {
          case 0 -> EmployeeProcessor
            .ListProcessor.process(getScanner(), repository()::getAll, getDepartmentRepository()::getAll);
          case 1 -> showSalaryAverage();
          case 2 -> giveSalaryRaise();
          case 3 -> EmployeeProcessor.CreateAndUpdateProcessor.create(
            getScanner(), repository()::add,
            getDepartmentRepository()::add,
            getDepartmentRepository()::getAll
          );
          case 4 -> EmployeeProcessor.CreateAndUpdateProcessor.update(
            getScanner(), repository()::update,
            getDepartmentRepository()::add,
            repository()::getAll,
            getDepartmentRepository()::getAll
          );
          default -> EmployeeProcessor.DestroyProcessor
            .remove(getScanner(), repository()::getAll, repository()::remove);
        }
      } catch (IllegalArgumentException e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }
  }
}
