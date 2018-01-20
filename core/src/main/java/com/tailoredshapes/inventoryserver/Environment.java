package com.tailoredshapes.inventoryserver;


public class Environment {
  public static String protocol = System.getenv("INV_PROTOCOL") != null ? System.getenv("INV_PROTOCOL") : "http" ;
  public static String host = System.getenv("INV_HOST") != null ? System.getenv("INV_HOST") : "localhost" ;
  public static int port = System.getenv("INV_PORT") != null ? Integer.parseInt(System.getenv("INV_PORT")) : 5555 ;
}
