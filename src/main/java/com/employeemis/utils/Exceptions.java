package com.employeemis.utils;

public class Exceptions {
  public static class InvalidSalaryException extends IllegalArgumentException {
    public InvalidSalaryException() {
      super("Salary cannot be below 1");
    }
  }
  public static class InvalidYearsOfExperienceException extends IllegalArgumentException {
    public InvalidYearsOfExperienceException() {
      super("Years of experience cannot be below 0");
    }
  }
  public static class InvalidPerformanceRateException extends IllegalArgumentException {
    public InvalidPerformanceRateException() {
      super("Performance rate must vary between 0 - 5");
    }
  }

  public static class ResourceNotFoundException extends IllegalAccessException {
    public <T> ResourceNotFoundException(String resource, T key) {
      super(String.format("No %s with id~%s found", resource, key));
    }
  }

  public static class DynamicUpdateException extends IllegalArgumentException {
    public DynamicUpdateException(String message) {
      super(message);
    }
  }

  public static class UniqueConstraintViolationException extends IllegalArgumentException {
    public <T> UniqueConstraintViolationException(String resource, String attributeName, T value) {
      super(String.format("%s with %s=`%s` already exists", resource, attributeName, value));
    }
  }
}
