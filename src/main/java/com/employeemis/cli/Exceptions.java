package com.employeemis.cli;

import java.util.Objects;

public class Exceptions {
  public static class AbortException extends Exception {
    public AbortException(String message) {
      super(Objects.isNull(message) ? "Going back" : message);
    }
  }
}
