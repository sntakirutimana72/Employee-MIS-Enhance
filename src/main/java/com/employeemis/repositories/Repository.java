package com.employeemis.repositories;

import com.employeemis.utils.Exceptions;

import java.util.List;

public interface Repository<K, V> {
  V get(K entityId) throws Exceptions.ResourceNotFoundException;
  void add(V entity) throws Exceptions.UniqueConstraintViolationException;
  void remove(K entityId) throws Exceptions.ResourceNotFoundException;
  <T> void update(K entityId, String field, T value) throws Exceptions.DynamicUpdateException;
  List<V> getAll();
}
