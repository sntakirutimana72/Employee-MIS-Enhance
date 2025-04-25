package com.employeemis.repositories;

import com.employeemis.models.User;
import com.employeemis.utils.Exceptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
  private UserRepository repository;

  @BeforeEach
  void setUp() {
    repository = new UserRepository();
  }

  @AfterEach
  void tearDown() {
    repository = null;
  }

  @Test
  void shouldRegardUniqueConstraintOnUsername() {
    repository.add(new User("testUser", "", 1));
    assertThrows(Exceptions.UniqueConstraintViolationException.class, () -> {
      repository.add(new User("testUser", "", 2));
    });
  }
}