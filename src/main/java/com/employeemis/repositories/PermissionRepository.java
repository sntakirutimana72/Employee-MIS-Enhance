package com.employeemis.repositories;

import com.employeemis.models.Permission;

import java.util.Objects;

public class PermissionRepository extends RepositoryAbstract<Integer, Permission> {
  @Override
  protected void enforceUniqueConstraint(Permission perm) throws IllegalArgumentException {
    for (Permission other : getAll())
      if (Objects.equals(perm.getName().toLowerCase(), other.getName().toLowerCase()))
        throw new IllegalArgumentException(
          String.format("%s with name=`%s` already exists", perm.getClass().getName(), perm.getName()));
  }
}
