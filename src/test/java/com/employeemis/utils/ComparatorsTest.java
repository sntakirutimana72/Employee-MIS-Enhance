package com.employeemis.utils;

import com.employeemis.models.Department;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ComparatorsTest {
  @Nested
  class Employee {
    private List<com.employeemis.models.Employee<Integer>> employees;

    @BeforeEach
    void setU() {
      Department<Integer> dept = new Department<>("HR");
      employees = new ArrayList<>(List.of(
        new com.employeemis.models.Employee<>(1, "first", dept, 19, 4, 1.3),
        new com.employeemis.models.Employee<>(2, "second", dept, 10, 7, 3.3),
        new com.employeemis.models.Employee<>(3, "third", dept, 13, 2, 3.1)
      ));
    }

    @AfterEach
    void tearDown() {
      employees = null;
    }

    @Test
    @DisplayName("Should sort by experience in descending order")
    void byExperienceDesc() {
      List<com.employeemis.models.Employee<Integer>> employeeList = employees.stream()
        .sorted(Comparators.Employee.byExperienceDesc()).collect(Collectors.toList());
      assertEquals(List.of(employees.get(1), employees.get(0), employees.get(2)), employeeList);
    }

    @Test
    @DisplayName("Should sort by performanceRate in descending order")
    void byPerformanceDesc() {
      List<com.employeemis.models.Employee<Integer>> employeeList = employees.stream()
        .sorted(Comparators.Employee.byPerformanceDesc()).collect(Collectors.toList());
      assertEquals(List.of(employees.get(1), employees.get(2), employees.get(0)), employeeList);
    }

    @Test
    @DisplayName("Should sort by salary in descending order")
    void bySalaryDesc() {
      List<com.employeemis.models.Employee<Integer>> employeeList = employees.stream()
        .sorted(Comparators.Employee.bySalaryDesc()).collect(Collectors.toList());
      assertEquals(List.of(employees.get(0), employees.get(2), employees.get(1)), employeeList);
    }
  }
}