package com.employeemis.cli;

import com.employeemis.repositories.DepartmentRepository;
import com.employeemis.repositories.EmployeeRepository;
import com.employeemis.repositories.PermissionRepository;
import com.employeemis.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
  private Integer sessionId;
  private final Scanner scanner;
  private final UserRepository userRepository;
  private final EmployeeRepository<Integer> employeeRepository;
  private final DepartmentRepository<Integer> departmentRepository;
  private final PermissionRepository permissionRepository;

  private Main() {
    super();

    userRepository = new UserRepository();
    employeeRepository = new EmployeeRepository<>();
    departmentRepository = new DepartmentRepository<>();
    permissionRepository = new PermissionRepository();
    scanner = new Scanner(System.in);

    Helpers.Printer.alert("Welcome To Employee Management System");
  }

  public boolean isLoggedIn() {
    return !Objects.isNull(sessionId);
  }

  public PermissionRepository getPermissionRepository() {
    return permissionRepository;
  }

  public UserRepository getUserRepository() {
    return userRepository;
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
      int choice;
      try {
        if (isLoggedIn()) {
          choice = Helpers.Selectors.select(
            "Select option", getScanner(), new ArrayList<>(
              List.of("Users", "Employees", "Departments", "Permissions", "Logout", "Exit")
            )
          );
          switch (choice) {
            case 0 -> new com.employeemis.cli.controllers.Users(this).process();
            case 1 -> new com.employeemis.cli.controllers.Employees(this).process();
            case 2 -> new com.employeemis.cli.controllers.Departments(this).process();
            case 3 -> new com.employeemis.cli.controllers.Permissions(this).process();
            case 4 -> sessionId = null;
            case 5 -> Helpers.Policies.exist("exit");
          }
        } else {
          choice = Helpers.Selectors.select(
            "Select option", getScanner(), new ArrayList<>(List.of("Login", "Exit")));
          if (choice == 0)
            sessionId = new com.employeemis.cli.controllers.Login(this).post();
          else
            Helpers.Policies.exist("exit");
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
