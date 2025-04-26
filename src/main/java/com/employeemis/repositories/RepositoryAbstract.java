package com.employeemis.repositories;

import com.employeemis.models.Trackable;
import com.employeemis.utils.Common;
import com.employeemis.utils.Exceptions;
import com.employeemis.utils.Loggers;

import java.lang.reflect.Method;
import java.util.*;

public abstract class RepositoryAbstract<K, V extends Trackable<K>> implements Repository<K, V> {
  private final Map<K, V> repository;

  public RepositoryAbstract() {
    repository = new HashMap<>();
  }

  protected String getEntityClassName() {
    return this.getClass().getName().replace("Repository", "");
  }

  @Override
  public V get(K key) throws Exceptions.ResourceNotFoundException {
    V entity = repository.get(key);
    if (Objects.isNull(entity))
      throw new Exceptions.ResourceNotFoundException(getEntityClassName(), key);
    return entity;
  }

  protected void enforceUniqueConstraint(V entity) throws Exceptions.UniqueConstraintViolationException {
    K uid = entity.getId();
    if (repository.containsKey(uid))
      throw new Exceptions.UniqueConstraintViolationException(entity.getClass().getName(), "id", uid);
  }

  @Override
  public void add(V entity) throws Exceptions.UniqueConstraintViolationException {
    enforceUniqueConstraint(entity);
    repository.put(entity.getId(), entity);
  }

  @Override
  public void remove(K key) throws Exceptions.ResourceNotFoundException {
    if (Objects.isNull(repository.remove(key)))
      throw new Exceptions.ResourceNotFoundException(getEntityClassName(), key);
  }

  @Override
  public <T> void update(K key, String attribute, T value) throws Exceptions.DynamicUpdateException {
    try {
      // Query entity to be updated
      V entity = this.get(key);
      // Find setter whose signature match the given parameter value type
      Method setter = Common.MethodFinders.hasSetter(entity.getClass(), attribute, value);
      // If found, invoke setter to update entity data
      setter.invoke(entity, value);
      // Log activity to console
      Loggers.BasicLogger.info(
        getEntityClassName(),
        "UPDATE",
        String.format("%s value updated to NEW_VALUE=`%s`", attribute, value));
    } catch (Exception e) {
      Loggers.BasicLogger.error(getEntityClassName(), "UPDATE", e);
      throw new Exceptions.DynamicUpdateException(e.getMessage());
    }
  }

  @Override
  public List<V> getAll() {
    return repository.values().stream().toList();
  }
}
