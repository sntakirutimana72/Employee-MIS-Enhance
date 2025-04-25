package com.employeemis.utils;

import java.util.Comparator;

public class Comparators {
  public static class Employee {
    public static <K> Comparator<com.employeemis.models.Employee<K>> byExperienceDesc() {
      return Comparator.comparingInt(com.employeemis.models.Employee<K>::getYearsOfExperience).reversed();
    }

    public static <K> Comparator<com.employeemis.models.Employee<K>> byPerformanceDesc() {
      return Comparator.comparingDouble(com.employeemis.models.Employee<K>::getPerformanceRate).reversed();
    }

    public static <K> Comparator<com.employeemis.models.Employee<K>> bySalaryDesc() {
      return Comparator.comparingDouble(com.employeemis.models.Employee<K>::getSalary).reversed();
    }
  }
}
