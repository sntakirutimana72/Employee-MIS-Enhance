package com.employeemis.models;

import java.time.LocalDate;

public interface Trackable<T> {
  // Get Unique ID
  T getId();

  // Creation date
  LocalDate getCreatedAt();

  // Last update date
  LocalDate getUpdatedAt();
}
