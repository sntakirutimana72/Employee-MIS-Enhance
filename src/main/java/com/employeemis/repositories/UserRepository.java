package com.employeemis.repositories;

import com.employeemis.models.User;
import com.employeemis.utils.Exceptions;

public class UserRepository extends RepositoryAbstract<Integer, User> {
  @Override
  protected void enforceUniqueConstraint(User user) throws Exceptions.UniqueConstraintViolationException {
    for (User other : getAll())
      if (user.getUsername().equalsIgnoreCase(other.getUsername()))
        throw new Exceptions.UniqueConstraintViolationException(user.getClass().getName(), "username", user.getUsername());
  }
}
