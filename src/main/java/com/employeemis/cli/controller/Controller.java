package com.employeemis.cli.controller;

import com.employeemis.repositories.Repository;

import java.util.Scanner;

public abstract class Controller {
  private final com.employeemis.cli.Main cliApplication;

  public Controller(com.employeemis.cli.Main cliApplication) {
    this.cliApplication = cliApplication;
  }

  public com.employeemis.cli.Main getCliApplication() {
    return cliApplication;
  }

  public Scanner getScanner() {
    return getCliApplication().getScanner();
  }

  abstract Repository<Integer, ?> repository();
}
