package com.tailoredshapes.inventoryserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;

public class InMemoryDataGridServer {
  static boolean started = false;
  static Logger logger = LoggerFactory.getLogger(InMemoryDataGridServer.class);

  public static void start() {
    Environment.port = 5555;
    logger.info("Starting test server on port " + Environment.port);
    if (!started) {

      started = true;
      port(Environment.port);
      InMemoryDataGridRouter.route();
    }
  }

  public static void resetDB() {
    InMemoryDataGridProviders.userDB.clear();
    InMemoryDataGridProviders.inventoryDB.clear();
    InMemoryDataGridProviders.categoryDB.clear();
    InMemoryDataGridProviders.metricDB.clear();
    InMemoryDataGridProviders.metricTypeDB.clear();
  }
}
