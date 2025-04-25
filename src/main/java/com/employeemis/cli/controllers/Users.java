package com.employeemis.cli.controllers;

import com.employeemis.cli.Helpers;
import com.employeemis.cli.Main;
import com.employeemis.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class Users extends Controller {
  public Users(Main app) {
    super(app);
  }

  @Override
  public UserRepository getRepository() {
    return getApp().getUserRepository();
  }

  public void process() throws Helpers.Errors.AbortException {
    Helpers.Printer.alert("User Menu");

    int choice = Helpers.Selectors.select("Select option", getApp().getScanner(), new ArrayList<>(List.of(
      "List All Users",
      "Create User",
      "Update User",
      "Change password"
    )));
  }
}
