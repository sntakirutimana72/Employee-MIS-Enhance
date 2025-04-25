package com.employeemis.repositories;

import com.employeemis.models.Trackable;

import java.lang.reflect.Method;
import java.util.*;

public abstract class RepositoryAbstract<K, V extends Trackable<K>> implements Repository<K, V> {
  private final Map<K, V> repository;

  public RepositoryAbstract() {
    repository = new HashMap<>();
  }

  @Override
  public V get(K key) throws NoSuchElementException {
    V entity = repository.get(key);
    if (Objects.isNull(entity))
      throw new NoSuchElementException("Resource not found");
    return entity;
  }

  protected void enforceUniqueConstraint(V entity) throws IllegalArgumentException {
    K uid = entity.getId();
    if (repository.containsKey(uid))
      throw new IllegalArgumentException(
        String.format("%s with id=`%s` already exists", entity.getClass().getName(), uid));
  }

  @Override
  public void add(V entity) {
    enforceUniqueConstraint(entity);
    repository.put(entity.getId(), entity);
  }

  @Override
  public void remove(K key) {
    repository.remove(key);
  }

  @Override
  public <T> void update(K key, String attribute, T value) throws IllegalArgumentException {
    V entity = this.get(key);
    try {
      Method setter = com.employeemis.utils.Common.hasSetter(entity.getClass(), attribute, value);
      setter.invoke(entity, value);
    } catch (Exception e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  @Override
  public List<V> getAll() {
    return repository.values().stream().toList();
  }
}
