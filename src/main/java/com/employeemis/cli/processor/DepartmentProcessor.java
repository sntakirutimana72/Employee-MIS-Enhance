package com.employeemis.cli.processor;

import com.employeemis.cli.Exceptions;
import com.employeemis.cli.Helpers;
import com.employeemis.cli.actions.EntityDynamicUpdater;
import com.employeemis.cli.actions.EntityRemover;
import com.employeemis.models.Department;
import static com.employeemis.utils.Exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class DepartmentProcessor {

  public static class ListProcessor {
    private static void list(List<Department<Integer>> departments) {
      Helpers.Printer.tabular(
        "List of Departments",
        List.of("ID", "Name", "Number of Members", "Creation Date", "Last Update"),
        departments.stream().map((e) -> List.of(
          String.valueOf(e.getId()),
          e.getName(),
          String.valueOf(e.getEmployees().size()),
          e.getCreatedAt().toString(),
          e.getUpdatedAt().toString()
        )).toList()
      );
    }

    public static void process(Supplier<List<Department<Integer>>> queryAll) {
      try {
        List<Department<Integer>> departments = Helpers.Policies.cannotBeEmpty("department", queryAll);
        list(departments);
      } catch (Exception e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }
  }

  public static class CRUDProcessor {
    private static Department<Integer> selectDepartment(Scanner sc, Supplier<List<Department<Integer>>> queryAll) throws Exceptions.AbortException {
      List<Department<Integer>> departments = Helpers.Policies.cannotBeEmpty("department", queryAll);
      int index = Helpers.Selectors.selectEntity("department", sc, departments);

      return departments.get(index);
    }

    public static void remove(Scanner sc, EntityRemover<Integer> remover, Supplier<List<Department<Integer>>> queryAll) {
      try {
        Department<Integer> department = selectDepartment(sc, queryAll);
        remover.accept(department.getId());
        Helpers.Printer.alert("Department deleted successfully!!");
      } catch (ResourceNotFoundException | Exceptions.AbortException e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }

    public static void update(Scanner sc, EntityDynamicUpdater<Integer> updater, Supplier<List<Department<Integer>>> queryAll) {
      Helpers.Printer.alert("Update department");
      while (true) {
        try {
          Department<Integer> department = selectDepartment(sc, queryAll);
          String name = Helpers.Prompt.getText(sc, "Enter name:\n> ");

          updater.apply(department.getId(), "name", name);
          Helpers.Printer.alert(String.format("Department with ID~%s was successfully updated!", department.getId()));
          return;
        } catch (IllegalArgumentException e) {
          Helpers.Printer.alert(e.getMessage());
        } catch (Exceptions.AbortException e) {
          Helpers.Printer.alert(e.getMessage());
          return;
        }
      }
    }

    public static void create(Scanner sc, Consumer<Department<Integer>> saver) {
      Helpers.Printer.alert("Create new department");
      while (true) {
        try {
          String name = Helpers.Prompt.getText(sc, "Enter department name:\n> ");
          Department<Integer> department = new Department<>(name);

          saver.accept(department);
          Helpers.Printer.alert("Department successfully created!!");
          return;
        } catch (IllegalArgumentException e) {
          Helpers.Printer.alert(e.getMessage());
        } catch (Exceptions.AbortException e) {
          Helpers.Printer.alert(e.getMessage());
          return;
        }
      }
    }
  }
}
