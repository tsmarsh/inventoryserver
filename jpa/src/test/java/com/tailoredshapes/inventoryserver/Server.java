package com.tailoredshapes.inventoryserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;
import static spark.Spark.stop;

public class Server {
  static boolean started = false;
  static Logger logger = LoggerFactory.getLogger(Server.class);

  public static void start() {
    Environment.port = 5555;
    logger.info("Starting test server on port " + Environment.port);
    if (!started) {
      started = true;
        port(Environment.port);
      Router.route(TestPersistence.emf);
    }
  }
}
