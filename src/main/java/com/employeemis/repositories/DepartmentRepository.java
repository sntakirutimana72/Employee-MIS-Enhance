package com.employeemis.repositories;

import com.employeemis.models.Department;

import java.util.Objects;

public class DepartmentRepository<E> extends RepositoryAbstract<Integer, Department<E>> {
  @Override
  protected void enforceUniqueConstraint(Department<E> dept) throws IllegalArgumentException {
    for (Department<E> other : getAll())
      if (Objects.equals(dept.getName().toLowerCase(), other.getName().toLowerCase()))
        throw new IllegalArgumentException(
          String.format("%s with name=`%s` already exists", dept.getClass().getName(), dept.getName()));
  }

  @Override
  public void remove(Integer key) {
    if (!get(key).getEmployees().isEmpty())
      throw new IllegalArgumentException("Cannot delete department with employees");
    super.remove(key);
  }
}
