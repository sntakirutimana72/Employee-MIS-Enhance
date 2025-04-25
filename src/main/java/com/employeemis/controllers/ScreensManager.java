package com.employeemis.controllers;

import com.employeemis.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ScreensManager {
  private final Main app;
  private final StackPane container; // Parent container to hold views
  private final Map<String, Node> screens = new HashMap<>();
  private final Map<String, Controller> controllers = new HashMap<>();

  public ScreensManager(Main app, StackPane container) {
    this.app = app;
    this.container = container;
  }

  private Node load(String name) throws IOException {
    FXMLLoader loader = new FXMLLoader(
      getClass().getResource("/com/employeemis/" + name + ".fxml")
    );
    Node screen = loader.load();
    Controller controller = loader.getController();

    controller.setApplication(app);
    screens.put(name, screen);
    controllers.put(name, controller);

    return screen;
  }

  public void switchTo(String name) {
    try {
      Node screen = screens.get(name);
      if (Objects.isNull(screen))
        screen = load(name);
      container.getChildren().setAll(screen);
    } catch (IOException e) {
      //noinspection CallToPrintStackTrace
      e.printStackTrace();
    }
  }
}

