package com.employeemis.cli.controller;

import com.employeemis.cli.Exceptions;
import com.employeemis.cli.Helpers;
import com.employeemis.cli.Main;
import com.employeemis.cli.processor.DepartmentProcessor;
import com.employeemis.repositories.DepartmentRepository;

import java.util.List;

public class DepartmentController extends Controller {

  public DepartmentController(Main cliApplication) {
    super(cliApplication);
  }

  @Override
  public DepartmentRepository<Integer> repository() {
    return getCliApplication().getDepartmentRepository();
  }

  public void process() throws Exceptions.AbortException {
    Helpers.Printer.alert("Department Menu");
    //noinspection InfiniteLoopStatement
    while (true) {
      try {
        int choice = Helpers.Selectors.select("Select option", getScanner(), List.of(
          "List Departments",
          "Create Department",
          "Update Department",
          "Delete Department"
        ));

        switch (choice) {
          case 0 -> DepartmentProcessor.ListProcessor.process(repository()::getAll);
          case 1 -> DepartmentProcessor.CRUDProcessor.create(getScanner(), repository()::add);
          case 2 -> DepartmentProcessor.CRUDProcessor.update(
            getScanner(),
            repository()::update, repository()::getAll
          );
          default -> DepartmentProcessor.CRUDProcessor.remove(
            getScanner(),
            repository()::remove, repository()::getAll
          );
        }
      } catch (IllegalArgumentException e) {
        Helpers.Printer.alert(e.getMessage());
      }
    }
  }
}
