package com.employeemis.cli;

import com.employeemis.models.Entity;
import com.employeemis.models.Nameable;
import com.employeemis.models.Trackable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;

public class Helpers {

  public static class Policies {
    public static void exist(String userInput) {
      if (userInput.equals("exit")) {
        Printer.alert("Application existing...");
        System.exit(0);
      }
    }

    public static <K, E extends Entity<K>> List<E> cannotBeEmpty(String tag, Supplier<List<E>> queryAll) {
      List<E> records = queryAll.get();
      if (records.isEmpty())
        throw new IllegalArgumentException("No " + tag + " records found");
      return records;
    }

    public static void abort(String userInput) throws Exceptions.AbortException {
      if (userInput.equals("..."))
        throw new Exceptions.AbortException(null);
    }
  }

  public static class Prompt {
    public static String getText(Scanner scanner, String tag) throws Exceptions.AbortException {
      System.out.print(tag);
      String userInput = scanner.nextLine();

      Policies.exist(userInput);
      Policies.abort(userInput);

      return userInput;
    }

    public static int getInt(Scanner scanner, String tag) throws Exceptions.AbortException {
      return Integer.parseInt(getText(scanner, tag));
    }

    public static int getPositiveInt(Scanner scanner, String tag, int min) throws Exceptions.AbortException {
      int value = getInt(scanner, tag);
      if (value < Math.max(0, min))
        throw new IllegalArgumentException("Value must be a positive value >= " + Math.max(0, min));
      return value;
    }

    public static double getDouble(Scanner scanner, String tag) throws Exceptions.AbortException {
      return Double.parseDouble(getText(scanner, tag));
    }
  }

  public static class Printer {
    public static void list(String label, List<String> items) {
      // Show guiding message before printing options
      System.out.println("*** " + label + " ***");
      for (int i = 0; i < items.size(); i++)
        System.out.printf(" %d. %s%n", i + 1, items.get(i));
    }

    public static void tabular(String title, List<String> columns, List<List<String>> rows) {
      // Calculate max width for each column
      List<Integer> colWidths = new ArrayList<>();
      for (int i = 0; i < columns.size(); i++) {
        int max = columns.get(i).length();
        for (List<String> row : rows) {
          max = Math.max(max, row.get(i).length());
        }
        colWidths.add(max);
      }

      // Print header
      int padding = colWidths.stream().mapToInt(Integer::intValue).sum() + columns.size() * 3 - 1;
      String bar = "+" + "-".repeat(padding) + "+";
      System.out.println(bar);
      System.out.printf(
        "|%s%s%s|%n", " ".repeat((padding - title.length()) / 2), title,
        " ".repeat(Double.valueOf(Math.ceil((padding - title.length()) / 2.0)).intValue()));
      System.out.println(bar);

      // Print column names
      for (int i = 0; i < columns.size(); i++) {
        System.out.printf("| %-" + colWidths.get(i) + "s ", columns.get(i));
      }
      System.out.println("|");

      // Print separator
      for (int width : colWidths) {
        System.out.print("+");
        System.out.print("-".repeat(width + 2));
      }
      System.out.println("+");

      // Print rows
      for (List<String> row : rows) {
        for (int i = 0; i < columns.size(); i++) {
          String cell = i < row.size() ? row.get(i) : "";
          System.out.printf("| %-" + colWidths.get(i) + "s ", cell);
        }
        System.out.println("|");
      }

      // Print bottom bar
      System.out.println(bar);
    }

    public static void alert(String message) {
      int dashing = Math.max(message.length() + 4, 50);
      int padding = dashing - message.length();
      String x = "+" + "-".repeat(dashing) + "+";
      System.out.println(x);
      System.out.printf(
        "|%s%s%s|%n",
        " ".repeat(padding / 2), message,
        " ".repeat(Double.valueOf(Math.ceil(padding / 2.0)).intValue()));
      System.out.println(x);
    }
  }

  public static class Selectors {
    public static int select(String tag, Scanner scanner, List<String> items) throws Exceptions.AbortException {
      while (true) {
        try {
          // Display available options
          Printer.list(tag, items);
          // Now, get user choice
          int selection = Prompt.getInt(scanner, "> ");
          // Check if given choice is acceptable
          if (selection < 1 || selection > items.size())
            throw new IllegalArgumentException("Invalid choice");
          return selection - 1;
        } catch (IllegalArgumentException e) {
          Printer.alert(e.getMessage());
        }
      }
    }

    public static <E extends Nameable & Trackable<Integer>> int selectEntity(String tag, Scanner scanner, List<E> items) throws Exceptions.AbortException {
      return select(
        String.format("Select %s", tag),
        scanner,
        items.stream().map(E::getName).toList()
      );
    }
  }
}
