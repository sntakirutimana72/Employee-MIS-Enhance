package com.employeemis.models;

import com.employeemis.utils.Validators;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Objects;

public class Employee<T> extends ActivableEntity<T> implements Employable<T> {
  // Contracted from Employable interface
  private String name;
  private Department<T> department;
  private double salary;
  private int yearsOfExperience;
  private double performanceRate;

  public Employee(T id, String name, Department<T> department, double salary, int yearsOfExperience, double performanceRate) {
    super(id);
    setName(name);
    assignDepartment(department);
    setSalary(salary);
    setYearsOfExperience(yearsOfExperience);
    setPerformanceRate(performanceRate);
    setIsActive(true);
  }

  @Override
  public void setName(String name) throws IllegalArgumentException {
    Validators.Employee.validateName(name);
    this.name = name;
  }

  private void assignDepartment(Department<T> department) {
    this.department = department;
    if (Objects.isNull(department))
      return;
    department.addEmployee(this);
  }

  @Override
  public double giveSalaryRaise() {
    if (getPerformanceRate() >= 4.5)
      setSalary(getSalary() * 1.3);
    return getSalary();
  }

  @Override
  public void setDepartment(Department<T> department) {
    if (this.department == department)
      return;
    Department<T> previous = this.department;
    assignDepartment(department);
    if (!Objects.isNull(previous))
      previous.removeEmployee(this);
  }

  @Override
  public void setSalary(double salary) throws IllegalArgumentException {
    Validators.Employee.validateSalary(salary);
    this.salary = salary;
  }

  @Override
  public void setYearsOfExperience(int yearsOfExperience) throws IllegalArgumentException {
    Validators.Employee.validateYearsOfExperience(yearsOfExperience);
    this.yearsOfExperience = yearsOfExperience;
  }

  @Override
  public void setPerformanceRate(double performanceRate) throws IllegalArgumentException {
    Validators.Employee.validatePerformanceRate(performanceRate);
    this.performanceRate = performanceRate;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Department<T> getDepartment() {
    return department;
  }

  @Override
  public double getSalary() {
    return salary;
  }

  @Override
  public int getYearsOfExperience() {
    return yearsOfExperience;
  }

  @Override
  public double getPerformanceRate() {
    return performanceRate;
  }

  @Override
  public int compareTo(Employable<T> emp) {
    return Integer.compare(emp.getYearsOfExperience(), this.getYearsOfExperience());
  }
}
