package com.employeemis.repositories;

import java.util.List;
import java.util.NoSuchElementException;

public interface Repository<K, V> {
  V get(K entityId) throws NoSuchElementException;
  void add(V entity);
  void remove(K entityId);
  <T> void update(K entityId, String field, T value) throws Exception;
  List<V> getAll();
}
