package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class InventoryServer {
    public static void main(String... args) throws IOException {
        Injector injector = Guice.createInjector(new InventoryModule("0.0.0.0", 5555));
        HttpServer server = injector.getInstance(HttpServer.class);

        server.start();
    }
}
