package com.employeemis.models;

public enum PermissionActions {
  READ("read"),
  EDIT("edit"),
  CREATE("create"),
  DESTROY("destroy"),
  ALL("all");

  private final String tag;

  PermissionActions(String tag) {
    this.tag = tag;
  }

  public String getTag() {
    return tag;
  }

  @Override
  public String toString() {
    return this.tag;
  }
}
