package com.tailoredshapes.inventoryserver;

import java.util.logging.Logger;

import static com.tailoredshapes.inventoryserver.SparkUtils.createServerWithRequestLog;
import static spark.Spark.port;

public class App {

  private static Logger logger = Logger.getLogger(App.class.getName());

  public static void main(String... args) {
    createServerWithRequestLog(logger);
    port(Environment.port);
    InMemoryRouter.route();
  }
}
