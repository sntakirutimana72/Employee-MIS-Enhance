package com.employeemis.cli;

import com.employeemis.cli.controller.EmployeeController;
import com.employeemis.cli.controller.DepartmentController;
import com.employeemis.models.Department;
import com.employeemis.models.Employee;
import com.employeemis.repositories.DepartmentRepository;
import com.employeemis.repositories.EmployeeRepository;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
  private final Scanner scanner;
  private final EmployeeRepository<Integer> employeeRepository;
  private final DepartmentRepository<Integer> departmentRepository;

  private Main() {
    super();
    employeeRepository = new EmployeeRepository<>();
    departmentRepository = new DepartmentRepository<>();
    scanner = new Scanner(System.in);

    loadInitialData();
    Helpers.Printer.alert("Welcome To Employee Management System");
  }

  private void loadInitialData() {
    // Add department dummy state data
    departmentRepository.add(new Department<>("hr"));
    departmentRepository.add(new Department<>("it"));
    departmentRepository.add(new Department<>("customer care"));

    // Add employee dummy state data
    try {
      employeeRepository.add(new Employee<>(
        1, "joe", departmentRepository.get(1), 2.4, 3, 4.1));
      employeeRepository.add(new Employee<>(
        2, "jean", departmentRepository.get(2), 75, 6, 3.1));
      employeeRepository.add(new Employee<>(
        3, "kim", departmentRepository.get(3), 25.3, 3, 2.21));
      employeeRepository.add(new Employee<>(
        4, "jim", departmentRepository.get(1), 17.3, 0, 2.3));
      employeeRepository.add(new Employee<>(
        5, "kenny", departmentRepository.get(3), 52.9, 5, 4.3));
      employeeRepository.add(new Employee<>(
        6, "Suzane", departmentRepository.get(2), 84.27, 1, 1.1));
    } catch (Exception ignored) {}
  }

  public EmployeeRepository<Integer> getEmployeeRepository() {
    return employeeRepository;
  }

  public DepartmentRepository<Integer> getDepartmentRepository() {
    return departmentRepository;
  }

  public Scanner getScanner() {
    return scanner;
  }

  public void run() {
    //noinspection InfiniteLoopStatement
    while (true) {
      try {
        int choice = Helpers.Selectors.select("Select option", getScanner(), List.of("Employee", "Department", "Exit"));
        switch (choice) {
          case 0 -> new EmployeeController(this).process();
          case 1 -> new DepartmentController(this).process();
          case 2 -> Helpers.Policies.exist("exit");
        }
      } catch (Exception e) {
        Helpers.Printer.alert(Objects.isNull(e.getMessage()) ? "" : e.getMessage());
      }
    }
  }

  public static void main(String[] args) {
    new Main().run();
  }
}
