package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;

public class InventoryServer {
    public static void main(String... args) throws IOException {
        Injector injector = Guice.createInjector(new InventoryServerModule("0.0.0.0", 5555));
        HttpServer server = injector.getInstance(HttpServer.class);

        server.start();
        System.out.println("Server started listening on 0.0.0.0:5555");
    }
}