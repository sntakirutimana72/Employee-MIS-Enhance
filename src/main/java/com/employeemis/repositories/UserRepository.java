package com.employeemis.repositories;

import com.employeemis.models.User;

import java.util.Objects;

public class UserRepository extends RepositoryAbstract<Integer, User> {
  @Override
  protected void enforceUniqueConstraint(User user) throws IllegalArgumentException {
    for (User other : getAll())
      if (Objects.equals(user.getUsername().toLowerCase(), other.getUsername().toLowerCase()))
        throw new IllegalArgumentException(
          String.format("%s with name=`%s` already exists", user.getClass().getName(), user.getUsername()));
  }
}
