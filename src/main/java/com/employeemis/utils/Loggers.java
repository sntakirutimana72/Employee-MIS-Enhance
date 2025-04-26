package com.employeemis.utils;

public class Loggers {
  public static class BasicLogger {
    private static void logging(System.Logger.Level loggingLevel, String scope, String context, String message) {
      System.getLogger(scope).log(loggingLevel, scope + "::" + context + " ~ " + message);
    }

    public static void error(String scope, String context, Exception e) {
      logging(System.Logger.Level.ERROR, scope, context, e.getMessage());
    }

    public static void info(String scope, String context, String message) {
      logging(System.Logger.Level.INFO, scope, context, message);
    }
  }
}
