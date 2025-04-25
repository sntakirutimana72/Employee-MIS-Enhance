package com.employeemis.utils;

public class Validators {
  public static class Employee {
    public static void validateName(String name) throws IllegalArgumentException {
      if (!name.matches("^[a-zA-Z]{3,}(\\s[a-zA-Z]{3,})*$"))
        throw new IllegalArgumentException("Invalid `name` value");
    }

    public static void validateYearsOfExperience(int yearsOfExperience) {
      if (yearsOfExperience < 0)
        throw new IllegalArgumentException("Years of experience cannot be below 0");
    }

    public static void validateSalary(double salary) {
      if (salary < 1)
        throw new IllegalArgumentException("Salary cannot be below 1");
    }

    public static void validatePerformanceRate(double performanceRate) {
      if (performanceRate < 0 || performanceRate > 5)
        throw new IllegalArgumentException("Performance rate must vary between 0-5");
    }
  }

  public static class Department {
    public static void validateName(String name) throws IllegalArgumentException {
      if (!name.matches("^[a-zA-Z0-9]{2,}((\\s|-|_)[a-zA-Z0-9]{2,})*$"))
        throw new IllegalArgumentException("Department name value is invalid");
    }
  }
}
