package com.employeemis.repositories;

import com.employeemis.models.Department;
import com.employeemis.models.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;

class EmployeeRepositoryTest {
  private EmployeeRepository<Integer> repository;

  private Employee<Integer> create(int id) {
    Department<Integer> department = new Department<>("HR");
    return new Employee<>(id, "john", department, 23, 2, 3);
  }

  @BeforeEach
  void setUp() {
    repository = new EmployeeRepository<>();
    repository.add(create(1));
  }

  @AfterEach
  void tearDown() {
    repository = null;
  }

  @Test
  void expectGetToThrowErrorWhenNoEmployeeFound() {
    assertThrows(NoSuchElementException.class, () -> repository.get(2));
  }

  @Test
  void expectGetToReturnEmployee() {
    assertInstanceOf(Employee.class, repository.get(1));
  }

  @Test
  void expectAddToStoreEmployeeById() {
    Employee<Integer> employee = create(2);
    repository.add(employee);
    assertEquals(employee, repository.get(employee.getId()));
  }

  @Test
  void expectDuplicateKeyToThrow() {
    assertThrows(IllegalArgumentException.class, () -> repository.add(create(1)));
  }

  @Test
  void expectRemoveToDeleteExistingEmployee() {
    Employee<Integer> empToBeDeleted = repository.get(1);
    Department<Integer> assignedDept = empToBeDeleted.getDepartment();

    assertTrue(assignedDept.getEmployees().stream().anyMatch(empToBeDeleted::equals));
    repository.remove(1);
    assertThrows(NoSuchElementException.class, () -> repository.get(1));
    assertTrue(assignedDept.getEmployees().stream().noneMatch(empToBeDeleted::equals));
    assertNull(empToBeDeleted.getDepartment());
  }

  @Test
  void expectUpdateToCallSetterOfGivenAttribute() throws Exception {
    repository.update(1, "name", "New Dynamic Value");
    assertEquals("New Dynamic Value", repository.get(1).getName());
  }

  @Test
  void expectUpdateToThrowWhenNoSetterFound() {
    assertThrows(Exception.class, () -> repository.update(1, "age", "123"));
  }

  @Test
  void expectUpdateToThrowDueToValidationError() {
    assertThrows(Exception.class, () -> repository.update(1, "name", "123"));
  }

  @Test
  void getAll() {
    repository.add(create(2));
    assertEquals(2, repository.getAll().size());
  }

  @Test
  @DisplayName("Should sort by salary in descending order and get top 5")
  void getTop5Paid() {
    Department<Integer> dept = new Department<>("Customer care");
    for (int i = 0; i < 6; i++)
      repository.add(new Employee<>(i + 4, "first", dept, (7 + i) * 1.12, 4, 1.3));
    List<Employee<Integer>> top5Paid = repository.getTop5Paid();
    assertTrue(repository.getAll().size() > 5);
    assertEquals(5, top5Paid.size());
    assertTrue(top5Paid.get(0).getSalary() >= top5Paid.get(1).getSalary());
    assertTrue(top5Paid.get(1).getSalary() >= top5Paid.get(4).getSalary());
  }

  @Test
  void expectGetSalaryAverageByDepartmentToReturnTheAverage() {
    Department<Integer> dept = new Department<>("Customer care");
    for (int i = 0; i < 3; i++)
      repository.add(new Employee<>(i + 4, "avg", dept, 15, 4, 1.3));
    assertEquals(15, repository.getSalaryAverageByDepartment("Customer care"));
  }

  @Test
  void expectGetSalaryAverageByDepartmentToReturnZeroWhenNoSuchDeptExists() {
    assertEquals(0, repository.getSalaryAverageByDepartment("Care"));
  }

  @Test
  void expectGetSalaryAverageByDepartmentToIgnoreCase() {
    assertEquals(23, repository.getSalaryAverageByDepartment("hr"));
  }
}