package com.employeemis.repositories;

import com.employeemis.models.Department;
import com.employeemis.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepartmentRepositoryTest {
  private DepartmentRepository<Integer> repository;

  @BeforeEach
  void setUp() {
    repository = new DepartmentRepository<>();
  }

  @AfterEach
  void tearDown() {
    repository = null;
  }

  @Test
  void shouldRegardUniqueConstraintOnName() {
    repository.add(new Department<>("HR"));
    assertThrows(IllegalArgumentException.class, () -> repository.add(new Department<>("HR")));
  }
}