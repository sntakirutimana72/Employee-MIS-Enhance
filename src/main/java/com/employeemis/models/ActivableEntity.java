package com.employeemis.models;

public abstract class ActivableEntity<T> extends Entity<T> implements Activable {
  private boolean isActive;

  public ActivableEntity(T id) {
    super(id);
  }

  @Override
  public void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  @Override
  public boolean getIsActive() {
    return isActive;
  }
}
