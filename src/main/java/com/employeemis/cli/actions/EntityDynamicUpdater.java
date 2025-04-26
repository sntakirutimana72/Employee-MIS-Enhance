package com.employeemis.cli.actions;

@FunctionalInterface
public interface EntityDynamicUpdater<K> {
  <V> void apply(K key, String attribute, V value);
}
