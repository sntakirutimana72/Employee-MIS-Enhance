package com.employeemis.cli.processor;

import com.employeemis.cli.Helpers;
import com.employeemis.cli.Exceptions;
import com.employeemis.cli.actions.EntityDynamicUpdater;
import com.employeemis.cli.actions.EntityRemover;
import com.employeemis.models.*;
import com.employeemis.utils.Comparators;
import com.employeemis.utils.Converters;
import com.employeemis.utils.Filters;

import static com.employeemis.utils.Exceptions.ResourceNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EmployeeProcessor {
  private static <E extends Entity<Integer> & Nameable> E selectEntity(Scanner sc, String tag, Supplier<List<E>> querySelector) throws Exceptions.AbortException {
    List<E> records = Helpers.Policies.cannotBeEmpty(tag, querySelector);
    int index = Helpers.Selectors.selectEntity(tag, sc, records);

    return records.get(index);
  }

  public static class CreateAndUpdateProcessor {
    private static Department<Integer> createDepartment(Scanner sc,
                                                        Consumer<Department<Integer>> saver
    ) throws Exceptions.AbortException {
      while (true) {
        try {
          String name = Helpers.Prompt.getText(sc, "Enter department name:\n> ");
          Department<Integer> department = new Department<>(name);
          saver.accept(department);
          return department;
        } catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
        }
      }
    }

    private static Department<Integer> selectOrCreateDepartment(Scanner sc,
                                                                Supplier<List<Department<Integer>>> querySelector,
                                                                Consumer<Department<Integer>> saver
    ) throws Exceptions.AbortException {
      return querySelector.get().isEmpty() ?
        createDepartment(sc, saver) :
        selectEntity(sc, "department", querySelector);
    }

    private static String selectUpdateAttribute(Scanner sc) throws Exceptions.AbortException {
      List<String> attributes = List.of("name", "department", "salary", "yearsOfExperience", "performanceRate");
      int index = Helpers.Selectors.select("Select field to be updated:", sc, attributes);

      return attributes.get(index);
    }

    public static void create(Scanner sc, Consumer<Employee<Integer>> saver,
                              Consumer<Department<Integer>> departmentSaver,
                              Supplier<List<Department<Integer>>> departmentQuerySelector
    ) {
      Helpers.Printer.alert("Create New Employee");
      while (true) {
        try {
          // Get employee ID
          int employeeId = Helpers.Prompt.getPositiveInt(sc, "Enter employee ID (must be a number):\n> ", 1);
          // Get full name
          String fullName = Helpers.Prompt.getText(sc, "Enter employee name:\n> ");
          // Select department
          Department<Integer> department = selectOrCreateDepartment(sc, departmentQuerySelector, departmentSaver);
          // Get salary
          double salary = Helpers.Prompt.getDouble(sc, "Enter salary:\n> ");
          // Get years of experience
          int yearsOfExperience = Helpers.Prompt.getPositiveInt(sc, "Enter years of experience:\n> ", 0);
          // Get performance rate
          double performance = Helpers.Prompt.getDouble(sc, "Enter performance rate:\n> ");

          // Now, create employee record
          Employee<Integer> employee = new Employee<>(
            employeeId,
            fullName,
            department,
            salary,
            yearsOfExperience,
            performance
          );
          saver.accept(employee);
          Helpers.Printer.alert("Employee created successfully!!");
          return;
        } catch (IllegalArgumentException e) {
          Helpers.Printer.alert(e.getMessage());
        } catch (Exceptions.AbortException e) {
          Helpers.Printer.alert(e.getMessage());
          return;
        }
      }
    }

    public static void update(Scanner sc, EntityDynamicUpdater<Integer> updater,
                              Consumer<Department<Integer>> departmentSaver,
                              Supplier<List<Employee<Integer>>> queryAll,
                              Supplier<List<Department<Integer>>> departmentQueryAll
    ) {
      // Select employee to work with
      Employee<Integer> employee;
      try {
        employee = selectEntity(sc, "employee", queryAll);
      } catch (Exceptions.AbortException e) {
        Helpers.Printer.alert(e.getMessage());
        return;
      }

      while (true) {
        try {
          // Get attribute to be updated
          String attribute = selectUpdateAttribute(sc);
          // Dispatch appropriate action given attribute value
          switch (attribute) {
            // This deals with department updates
            case "department" -> {
              Department<Integer> department = selectOrCreateDepartment(sc, departmentQueryAll, departmentSaver);
              // If selected department is the same as the current one, abort
              if (department.getId().equals(employee.getDepartment().getId()))
                throw new IllegalArgumentException("You selected the same department");
              updater.apply(employee.getId(), "department", department);
            }

            // Deals with name update
            case "name" -> {
              String name = Helpers.Prompt.getText(sc, "Enter value of `name`:\n> ");
              updater.apply(employee.getId(), "name", name);
            }

            // Deals with yearsOfExperience update
            case "yearsOfExperience" -> {
              int experience = Helpers.Prompt.getPositiveInt(sc, "Enter value of `yearsOfExperience`:\n> ", 0);
              updater.apply(employee.getId(), "yearsOfExperience", experience);
            }

            // Deals with performanceRate & salary updates
            default -> {
              double value = Helpers.Prompt.getDouble(sc, String.format("Enter value of `%s`:\n> ", attribute));
              updater.apply(employee.getId(), attribute, value);
            }
          }
          Helpers.Printer.alert(String.format("Employee with ID~(%s) was successfully updated!!", employee.getId()));
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

  public static class DestroyProcessor {
    public static void remove(Scanner sc, Supplier<List<Employee<Integer>>> queryAll, EntityRemover<Integer> remover) {
      try {
        Employee<Integer> employee = selectEntity(sc, "employee", queryAll);
        remover.accept(employee.getId());
        Helpers.Printer.alert("Employee with ID~(" + employee.getId() + ") was successfully deleted!!");
      } catch (ResourceNotFoundException | Exceptions.AbortException e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }
  }

  public static class ListProcessor {
    private static void list(String title, List<Employee<Integer>> employees) throws Exceptions.AbortException {
      if (employees.isEmpty())
        throw new Exceptions.AbortException("No employee records found");
      List<String> columns = List.of(
        "ID", "Name", "Department", "Salary", 
        "Years of Experience", "Performance Rate", "Creation Date", "Last Update");
      Function<Employee<Integer>, List<String>> getRow = (e) -> List.of(
        String.valueOf(e.getId()),
        e.getName(),
        e.getDepartment().getName(),
        String.valueOf(e.getSalary()),
        String.valueOf(e.getYearsOfExperience()),
        String.valueOf(e.getPerformanceRate()),
        e.getCreatedAt().toString(),
        e.getUpdatedAt().toString()
      );
      Helpers.Printer.tabular(title, columns, employees.stream().map(getRow).toList());
    }

    private static void isolatedList(String title, List<Employee<Integer>> employees) {
      try {
        list(title, employees);
      } catch (Exceptions.AbortException e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }
    
    private static class FilterProcessor {
      private static void byDepartment(Scanner sc, Supplier<List<Employee<Integer>>> queryAll,
                                       Supplier<List<Department<Integer>>> queryAllDepartments
      ) {
        try {
          Department<Integer> department = selectEntity(sc, "department", queryAllDepartments);
          list(
            "List of Employees in " + department.getName() + " department",
            Converters.toStream(Filters.Employee.byDepartment(queryAll.get(), department.getName())).toList());
        } catch (Exceptions.AbortException e) {
          Helpers.Printer.alert(e.getMessage());
        }
      }

      private static void byName(Scanner sc, Supplier<List<Employee<Integer>>> queryAll) {
        try {
          String name = Helpers.Prompt.getText(sc, "Enter name:\n> ");
          list(
            "List of Employees whose names contains `" + name + "`",
            Converters.toStream(Filters.Employee.byName(queryAll.get(), name)).toList()
          );
        } catch (Exceptions.AbortException e) {
          Helpers.Printer.alert(e.getMessage());
        }
      }

      private static void byPerformance(Scanner sc, Supplier<List<Employee<Integer>>> queryAll) {
        try {
          double rate = Helpers.Prompt.getDouble(sc, "Enter performance rate:\n> ");
          list(
            "List of Employees with Performance Rate >= " + rate,
            Converters.toStream(Filters.Employee.withPerformanceGreaterThanOrEqualTo(queryAll.get(), rate)).toList()
          );
        } catch (Exceptions.AbortException e) {
          Helpers.Printer.alert(e.getMessage());
        }
      }

      private static void bySalaryRange(Scanner sc, Supplier<List<Employee<Integer>>> queryAll) {
        while (true) {
          try {
            String range = Helpers.Prompt.getText(sc, "Enter salary range (eg: 123-320.5):\n> ").trim();
            // Confirm if provided value match range constraint
            if (!range.matches("^\\d+(\\.\\d+)?(\\s)*-(\\s)*\\d+(\\.\\d+)?$"))
              throw new IllegalArgumentException("Salary must be a range (eg: 200.5-420 OR 17-32.5 OR 13-26 OR 5.3-8.5)");
            // Convert range to an array of string
            String[] ranges = range.split("-");
            // If ranges are more than two
            if (ranges.length > 2)
              throw new IllegalArgumentException("Salary must be a range (eg: 200.5-420 OR 17-32.5 OR 13-26 OR 5.3-8.5)");
            double startRange = Double.parseDouble(ranges[0]);
            double endRange = Double.parseDouble(ranges[1]);
            list(
              "List of Employees with salary range of " + range.replace(" ", ""),
              Converters.toStream(Filters.Employee.bySalaryRange(queryAll.get(), startRange, endRange)).toList()
            );
            return;
          } catch (IllegalArgumentException e) {
            Helpers.Printer.alert(e.getMessage());
          } catch (Exceptions.AbortException e) {
            Helpers.Printer.alert(e.getMessage());
            return;
          }
        }
      }
      
      public static void process(Scanner sc, Supplier<List<Employee<Integer>>> queryAll,
                                 Supplier<List<Department<Integer>>> queryAllDepartments
      ) {
        while (true) {
          try {
            int choice = Helpers.Selectors.select("Select filter", sc, List.of(
              "Filter by department",
              "Filter by name",
              "Filter by salary range (eg: 75-500)",
              "Filter by performance rate (>= X)"
            ));

            switch (choice) {
              case 0 -> byDepartment(sc, queryAll, queryAllDepartments);
              case 1 -> byName(sc, queryAll);
              case 2 -> bySalaryRange(sc, queryAll);
              default -> byPerformance(sc, queryAll);
            }
          } catch (IllegalArgumentException e) {
            Helpers.Printer.alert(e.getMessage());
          } catch (Exceptions.AbortException e) {
            Helpers.Printer.alert(e.getMessage());
            return;
          }
        }
      }
    }
    
    private static class SortProcessor {
      public static void process(Scanner sc, Supplier<List<Employee<Integer>>> queryAll) {
        while (true) {
          try {
            int choice = Helpers.Selectors.select("Select option", sc, List.of(
              "Sort by years of experience (DESC)",
              "Sort by performance rate (DESC)",
              "Sort by salary (DESC)"
            ));

            Comparator<Employee<Integer>> comparator;
            switch (choice) {
              case 0 -> comparator = Comparators.Employee.byExperienceDesc();
              case 1 -> comparator = Comparators.Employee.byPerformanceDesc();
              default -> comparator = Comparators.Employee.bySalaryDesc();
            }
            isolatedList(
              "List of Employees",
              queryAll.get().stream().sorted(comparator).toList()
            );
          } catch (IllegalArgumentException e) {
            Helpers.Printer.alert(e.getMessage());
          } catch (Exceptions.AbortException e) {
            Helpers.Printer.alert(e.getMessage());
            return;
          }
        }
      }
    }

    public static void process(Scanner sc, Supplier<List<Employee<Integer>>> queryAll,
                               Supplier<List<Department<Integer>>> queryAllDepartments
    ) {
      while (true) {
        try {
          int choice = Helpers.Selectors.select("Select option", sc, List.of(
            "List All",
            "List Top 5 Paid",
            "Filter By",
            "Sort By (DESC)"
          ));
          
          switch (choice) {
            case 0 -> isolatedList("List of Employees", queryAll.get());
            case 1 -> isolatedList(
              "List of Top 5 Paid Employees",
              queryAll.get()
                .stream()
                .sorted(Comparator.comparingDouble(Employee<Integer>::getSalary).reversed())
                .limit(5)
                .toList());
            case 2 -> FilterProcessor.process(sc, queryAll, queryAllDepartments);
            default -> SortProcessor.process(sc, queryAll);
          }
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
