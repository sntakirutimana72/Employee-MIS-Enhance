package com.employeemis.utils;

import java.util.Iterator;
import java.util.List;

public class Filters {
  public static class Employee {
    public static <K> Iterator<com.employeemis.models.Employee<K>> byDepartment(List<com.employeemis.models.Employee<K>> employees, String tag) {
      return employees.stream()
        .filter(e -> e.getDepartment().getName().equalsIgnoreCase(tag))
        .iterator();
    }

    public static <K> Iterator<com.employeemis.models.Employee<K>> byName(List<com.employeemis.models.Employee<K>> employees, String pattern) {
      return employees.stream()
        .filter(e -> e.getName().toLowerCase().contains(pattern.toLowerCase()))
        .iterator();
    }

    public static <K> Iterator<com.employeemis.models.Employee<K>> withPerformanceGreaterThanOrEqualTo(List<com.employeemis.models.Employee<K>> employees, double rate) {
      return employees.stream()
        .filter(e -> e.getPerformanceRate() >= rate)
        .iterator();
    }

    public static <K> Iterator<com.employeemis.models.Employee<K>> bySalaryRange(List<com.employeemis.models.Employee<K>> employees, double min, double max) {
      return employees.stream()
        .filter(e -> e.getSalary() >= min && e.getSalary() <= max)
        .iterator();
    }
  }
}
