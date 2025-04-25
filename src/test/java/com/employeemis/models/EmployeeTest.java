package com.employeemis.models;

import org.junit.jupiter.api.*;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {
  private Employee<Integer> employee;

  @BeforeEach
  void setUp() {
    Department<Integer> department = new Department<>("IT");
    employee = new Employee<>(1, "Test", department, 20.3, 0, 4);
  }

  @AfterEach
  void tearDown() {
    employee = null;
  }

  @Test
  @DisplayName("Should be true")
  void testIsActiveAttributeIsSetToTrueByDefault() {
    assertTrue(employee.getIsActive());
  }

  @Test
  @DisplayName("Should have id set by default")
  void testIdAttributeIsSetByDefault() {
    assertTrue(employee.getId() > 0);
  }

  @Test
  @DisplayName("Should have getIsActive() method with boolean return type")
  void testHasGetIsActive() throws NoSuchMethodException {
    // Act
    Method method = Employee.class.getMethod("getIsActive");
    // Assert existence
    assertNotNull(method, "Method ::getIsActive() should exist");
    // Assert return type
    assertEquals(boolean.class, method.getReturnType(), "getIsActive() should return boolean");
  }

  @Test
  @DisplayName("Should have getUpdatedAt() method with LocalDate return type")
  void testHasGetUpdatedAt() throws NoSuchMethodException {
    // Act
    Method method = Employee.class.getMethod("getUpdatedAt");
    // Assert existence
    assertNotNull(method, "Method ::getUpdatedAt() should exist");
    // Assert return type
    assertEquals(LocalDate.class, method.getReturnType(), "getUpdatedAt() should return LocalDate");
  }

  @Test
  @DisplayName("Should have getCreatedAt() method with LocalDate return type")
  void testHasGetCreatedAt() throws NoSuchMethodException {
    // Act
    Method method = Employee.class.getMethod("getCreatedAt");
    // Assert existence
    assertNotNull(method, "Method ::getCreatedAt() should exist");
    // Assert return type
    assertEquals(LocalDate.class, method.getReturnType(), "getCreatedAt() should return LocalDate");
  }

  @Test
  @DisplayName("Should be able set department from department side")
  void testBelongsToFromDepartmentSide() {
    Department<Integer> previous = employee.getDepartment();
    assertTrue(previous.getEmployees().contains(employee));
    Department<Integer> current = new Department<>("HR");
    current.addEmployee(employee);
    assertEquals(employee.getDepartment().getId(), current.getId());
    assertFalse(previous.getEmployees().contains(employee));
  }

  @Test
  @DisplayName("Should be to update department from employee side")
  void testBelongsToFromSelf() {
    Department<Integer> previous = employee.getDepartment();
    assertTrue(previous.getEmployees().contains(employee));
    Department<Integer> current = new Department<>("HR");
    employee.setDepartment(current);
    assertEquals(current.getId(), employee.getDepartment().getId());
    assertFalse(previous.getEmployees().contains(employee));
    assertTrue(current.getEmployees().contains(employee));
  }

  @Test
  @DisplayName("Should sort employee by :yearsOfExperience in descending order")
  void testIsSortableByYearsOfExperience() {
    Department<Integer> dept = new Department<>("IT");
    List<Employee<Integer>> employees = Arrays.asList(
      new Employee<>(1, "first", dept, 100, 4, 5),
      new Employee<>(2, "two", dept, 100, 7, 5),
      new Employee<>(3, "three", dept, 100, 2, 5)
    );
    Collections.sort(employees);
    assertEquals(7, employees.get(0).getYearsOfExperience());
    assertEquals(4, employees.get(1).getYearsOfExperience());
    assertEquals(2, employees.get(2).getYearsOfExperience());
  }

  @Test
  @DisplayName("Should not give salary raise if performance rate < 4.5")
  void shouldNotGiveSalaryRaise() {
    double initialSalary = employee.getSalary();
    assertEquals(initialSalary, employee.giveSalaryRaise());
  }

  @Test
  @DisplayName("Should give salary raise if performance rate >= 4.5")
  void shouldGiveSalaryRaise() {
    double initialSalary = employee.getSalary();
    employee.setPerformanceRate(4.6);
    assertTrue(initialSalary < employee.giveSalaryRaise());
  }
}