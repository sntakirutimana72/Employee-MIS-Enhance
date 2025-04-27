package com.employeemis.repositories;

import com.employeemis.models.Employee;
import com.employeemis.utils.Exceptions;
import com.employeemis.utils.Validators;

import java.util.Comparator;
import java.util.List;

public class EmployeeRepository<K> extends RepositoryAbstract<K, Employee<K>> {
  public List<Employee<K>> getTop5Paid() {
    return getAll().stream()
      .sorted(Comparator.comparingDouble(Employee<K>::getSalary).reversed())
      .limit(5)
      .toList();
  }

  public void giveSalaryRaise(double performanceRate, double raisePercentage) {
    Validators.Employee.validatePerformanceRate(performanceRate);
    if (raisePercentage <= 0 || raisePercentage > 100)
      throw new IllegalArgumentException("Salary raise percentage must be `0 < X <= 100`");
    getAll().forEach(e -> e.raiseSalary(performanceRate, raisePercentage));
  }

  public double getSalaryAverageByDepartment(String departmentName) {
    return getAll().stream()
      .filter(e -> e.getDepartment().getName().equalsIgnoreCase(departmentName))
      .mapToDouble(Employee::getSalary)
      .average()
      .orElse(0.0); // default if no employee found
  }

  @Override
  public void remove(K key) throws Exceptions.ResourceNotFoundException {
    get(key).setDepartment(null);
    super.remove(key);
  }
}
