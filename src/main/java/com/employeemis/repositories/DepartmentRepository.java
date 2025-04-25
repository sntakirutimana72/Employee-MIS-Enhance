package com.employeemis.repositories;

import com.employeemis.models.Department;
import com.employeemis.utils.Exceptions;

public class DepartmentRepository<E> extends RepositoryAbstract<Integer, Department<E>> {
  @Override
  protected void enforceUniqueConstraint(Department<E> dept) throws Exceptions.UniqueConstraintViolationException {
    for (Department<E> other : getAll())
      if (dept.getName().equalsIgnoreCase(other.getName()))
        throw new Exceptions.UniqueConstraintViolationException(getClass().getName(), "name", dept.getName());
  }

  @Override
  public void remove(Integer key) throws Exceptions.ResourceNotFoundException {
    if (!get(key).getEmployees().isEmpty())
      throw new Exceptions.EntityBacktrackRefViolationException("department", "employees");
    super.remove(key);
  }
}
