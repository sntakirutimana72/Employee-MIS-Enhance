package com.employeemis.models;

public interface Nameable {
  // Get & set employee name
  String getName();
  void setName(String name) throws IllegalArgumentException;
}
