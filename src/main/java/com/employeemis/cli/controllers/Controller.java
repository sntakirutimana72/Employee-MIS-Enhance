package com.employeemis.cli.controllers;

import com.employeemis.repositories.Repository;

import java.util.Scanner;

public abstract class Controller {
  private final com.employeemis.cli.Main app;

  public Controller(com.employeemis.cli.Main app) {
    this.app = app;
  }

  public com.employeemis.cli.Main getApp() {
    return app;
  }

  public Scanner getScanner() {
    return getApp().getScanner();
  }

  abstract Repository<Integer, ?> getRepository();
}
