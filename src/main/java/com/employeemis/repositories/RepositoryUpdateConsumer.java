package com.employeemis.repositories;

@FunctionalInterface
public interface RepositoryUpdateConsumer<K, F, V> {
  void accept(K key, F field, V value);
}
