package com.employeemis.models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

class PermissionTest {
  private Permission perm;

  @BeforeEach
  void setUp() {
    perm = new Permission("Tenant", "all", PermissionActions.READ);
  }

  @AfterEach
  void tearDown() {
    perm = null;
  }

  @Test
  void testGetActionsMethodReturnsActionsAsArrayOfString() {
    EnumSet<PermissionActions> enumPermActions = EnumSet.allOf(PermissionActions.class);
    assertTrue(Arrays.stream(perm.getActions()).anyMatch(name -> enumPermActions.contains(PermissionActions.valueOf(name.toUpperCase()))));
  }

  @Test
  void testCanAssignMoreActions() {
    int previousSize = perm.getActions().length;
    perm.addActions(PermissionActions.EDIT, PermissionActions.CREATE);
    assertNotEquals(previousSize, perm.getActions().length);
  }

  @Test
  void textCanRemoveAction() {
    perm.addActions(PermissionActions.CREATE);
    perm.removeAction(PermissionActions.READ);
    assertFalse(Arrays.stream(perm.getActions()).toList().contains(PermissionActions.READ.getTag()));
  }
}