package com.employeemis.cli.actions;

import com.employeemis.utils.Exceptions;

@FunctionalInterface
public interface Fetcher<K, R> {
  R get(K key) throws Exceptions.ResourceNotFoundException;
}
