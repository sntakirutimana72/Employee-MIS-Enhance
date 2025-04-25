package com.employeemis.utils;

import java.util.Comparator;

public class Comparators {
  public static class Employee {
    public static Comparator<Integer> byExperienceDesc() {
      return Comparator.comparingInt(Integer::intValue).reversed();
    }

    public static Comparator<Double> byDoubleDesc() {
      return Comparator.comparingDouble(Double::doubleValue).reversed();
    }
  }
}
