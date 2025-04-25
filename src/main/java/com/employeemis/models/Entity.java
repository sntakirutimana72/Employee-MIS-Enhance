package com.employeemis.models;

import java.time.LocalDate;

public abstract class Entity<T> implements Trackable<T> {
  private final T id;
  private final LocalDate createdAt;
  private LocalDate updatedAt;

  public Entity(T id) {
    this.id = id;
    createdAt = LocalDate.now();
    setUpdatedAt();
  }

  protected void setUpdatedAt() {
    updatedAt = LocalDate.now();
  }

  @Override
  public T getId() {
    return id;
  }

  @Override
  public LocalDate getCreatedAt() {
    return createdAt;
  }

  @Override
  public LocalDate getUpdatedAt() {
    return updatedAt;
  }
}
