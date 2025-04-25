package com.employeemis.cli.controllers;

import com.employeemis.cli.Helpers;
import com.employeemis.cli.Main;
import com.employeemis.models.Department;
import com.employeemis.repositories.DepartmentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Departments extends Controller {
  public Departments(Main app) {
    super(app);
  }

  @Override
  public DepartmentRepository<Integer> getRepository() {
    return getApp().getDepartmentRepository();
  }

  private void list() {
    List<Department<Integer>> departments = getRepository().getAll();
    Helpers.Errors.cannotBeEmpty("department", departments.isEmpty());
    Function<Department<Integer>, List<String>> getRow = (e) -> new ArrayList<>(List.of(
      String.valueOf(e.getId()),
      e.getName(),
      String.valueOf(e.getEmployees().size()),
      e.getCreatedAt().toString(),
      e.getUpdatedAt().toString()
    ));
    Helpers.Printer.tabular(
      "List of all departments",
      new ArrayList<>(List.of("ID", "Name", "Number of Members", "Creation Date", "Last Update")),
      departments.stream().map(getRow).toList()
    );
  }

  private void create() throws Helpers.Errors.AbortException {
    Helpers.Printer.alert("Create new department record");
    while (true) {
      try {
        String name = Helpers.Prompt.getText(getScanner(), "Enter department name:\n> ");
        Department<Integer> department = new Department<>(name);

        getRepository().add(department);
        Helpers.Printer.alert("Department successfully created!!");
        return;
      } catch (IllegalArgumentException e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }
  }

  private void update() throws Helpers.Errors.AbortException {
    List<Department<Integer>> departments = getRepository().getAll();
    Helpers.Errors.cannotBeEmpty("departments", departments.isEmpty());
    while (true) {
      try {
        int selection = Helpers.Selectors.selectEntity("department", getScanner(), departments);
        String name = Helpers.Prompt.getText(getScanner(), "Enter name:\n >");

        getRepository().update(selection, "name", name);
        Helpers.Printer.alert(String.format("Department id~%s was successfully updated!", selection));
        return;
      } catch (IllegalArgumentException e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }
  }

  private void remove() throws Helpers.Errors.AbortException {
    List<Department<Integer>> departments = getRepository().getAll();
    Helpers.Errors.cannotBeEmpty("departments", departments.isEmpty());
    int selected = Helpers.Selectors.selectEntity("department", getScanner(), departments);

    getRepository().remove(selected);
    Helpers.Printer.alert("Department deleted successfully!!");
  }

  private void dispatch(int choice) throws Helpers.Errors.AbortException {
    switch (choice) {
      case 0 -> list();
      case 1 -> create();
      case 2 -> update();
      default -> remove();
    }
  }

  public void process() throws Helpers.Errors.AbortException {
    Helpers.Printer.alert("Department Menu");
    //noinspection InfiniteLoopStatement
    while (true) {
      try {
        int choice = Helpers.Selectors.select("Select option", getApp().getScanner(), new ArrayList<>(List.of(
          "List All Departments",
          "Create Department",
          "Update Department",
          "Delete Department"
        )));
        dispatch(choice);
      } catch (IllegalArgumentException e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }
  }
}
