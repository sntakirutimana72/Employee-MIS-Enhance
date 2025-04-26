package com.employeemis.cli.actions;

import com.employeemis.utils.Exceptions;

@FunctionalInterface
public interface EntityRemover<K> {
  void accept(K key) throws Exceptions.ResourceNotFoundException;
}
