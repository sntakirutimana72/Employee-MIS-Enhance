package com.employeemis.cli.controller;

import com.employeemis.cli.Helpers;
import com.employeemis.cli.Main;
import com.employeemis.cli.Exceptions;
import com.employeemis.cli.processor.EmployeeProcessor;
import com.employeemis.models.Department;
import com.employeemis.repositories.DepartmentRepository;
import com.employeemis.repositories.EmployeeRepository;
import static com.employeemis.utils.Exceptions.ResourceNotFoundException;

import java.util.ArrayList;
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

  private void showSalaryAverage() throws Exceptions.AbortException {
    List<Department<Integer>> departments = Helpers.Policies.cannotBeEmpty("department", getDepartmentRepository()::getAll);
    int choice = Helpers.Selectors.selectEntity("department", getScanner(), departments);

    try {
      String departmentName = getDepartmentRepository().get(choice).getName();
      double average = repository().getSalaryAverageByDepartment(departmentName);
      Helpers.Printer.alert(String.format("Salary Average in %s department is $%f", departmentName, average));
    } catch (ResourceNotFoundException e) {
      Helpers.Printer.alert(e.getMessage());
    }
  }

  public void process() throws Exceptions.AbortException {
    //noinspection InfiniteLoopStatement
    while (true) {
      try {
        int choice = Helpers.Selectors.select("Select option", getScanner(), new ArrayList<>(List.of(
          "List Employees",
          "Show Employee Salary Average (By Department)",
          "Create Employee",
          "Update Employee",
          "Delete Employee"
        )));
        switch (choice) {
          case 0 -> EmployeeProcessor
            .ListProcessor.process(getScanner(), repository()::getAll, getDepartmentRepository()::getAll);
          case 1 -> showSalaryAverage();
          case 2 -> EmployeeProcessor.CreateAndUpdateProcessor.create(
            getScanner(), repository()::add,
            getDepartmentRepository()::add,
            getDepartmentRepository()::getAll
          );
          case 3 -> EmployeeProcessor.CreateAndUpdateProcessor.update(
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
