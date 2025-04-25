package com.employeemis.models;

import java.util.HashSet;
import java.util.Set;

public class Department<T> extends Entity<Integer> implements Nameable {
  private static int autoIDCounter = 0;
  private String name;
  private final Set<Employee<T>> employees;

  public Department(String name) {
    super(++autoIDCounter);
    setName(name);

    employees = new HashSet<>();
  }

  public void addEmployee(Employee<T> employee) {
    if (employees.add(employee))
      employee.setDepartment(this);
  }

  public void removeEmployee(Employee<T> employee) {
    employees.remove(employee);
  }

  public Set<Employee<T>> getEmployees() {
    return employees;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return getName();
  }
}
