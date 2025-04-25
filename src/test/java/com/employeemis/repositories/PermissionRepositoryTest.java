package com.employeemis.repositories;

import com.employeemis.models.Permission;
import com.employeemis.utils.Exceptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PermissionRepositoryTest {
  private PermissionRepository repository;

  @BeforeEach
  void setUp() {
    repository = new PermissionRepository();
  }

  @AfterEach
  void tearDown() {
    repository = null;
  }

  @Test
  void shouldRegardUniqueConstraintOnName() {
    repository.add(new Permission("Tenant", "all"));
    assertThrows(Exceptions.UniqueConstraintViolationException.class, () -> {
      repository.add(new Permission("Tenant", "all"));
    });
  }
}