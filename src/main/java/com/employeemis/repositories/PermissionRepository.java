package com.employeemis.repositories;

import com.employeemis.models.Permission;
import com.employeemis.utils.Exceptions;

public class PermissionRepository extends RepositoryAbstract<Integer, Permission> {
  @Override
  protected void enforceUniqueConstraint(Permission perm) throws Exceptions.UniqueConstraintViolationException {
    for (Permission other : getAll())
      if (perm.getName().equalsIgnoreCase(other.getName()))
        throw new Exceptions.UniqueConstraintViolationException(getClass().getName(), "name", perm.getName());
  }
}
