package com.employeemis.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Permission extends Entity<Integer> implements Nameable {
  private static int autoIDCounter = 0;
  private String name;
  private final String resource;
  private final Set<PermissionActions> actions;

  public Permission(String name, String resource) {
    this(name, resource, PermissionActions.READ);
  }

  public Permission(String name, String resource, PermissionActions ... actions) {
    super(++autoIDCounter);
    this.resource = resource;
    this.actions = new HashSet<>();

    setName(name);
    addActions(actions);
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  public void addActions(PermissionActions ... actions) {
    this.actions.addAll(Arrays.stream(actions).toList());
  }

  public void removeAction(PermissionActions action) {
    actions.remove(action);
  }

  @Override
  public String getName() {
    return name;
  }

  public String getResource() {
    return resource;
  }

  public String[] getActions() {
    return actions.stream().map(PermissionActions::getTag).toArray(String[]::new);
  }
}
