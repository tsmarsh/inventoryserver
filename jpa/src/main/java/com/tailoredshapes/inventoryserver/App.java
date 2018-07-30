package com.tailoredshapes.inventoryserver;

import static spark.Spark.port;

public class App {

  public static void main(String... args) {
    port(Environment.port);
    Router.route(ProdPersistence.emf);
    System.out.println("*****PORT: " + Environment.port);
  }
}
