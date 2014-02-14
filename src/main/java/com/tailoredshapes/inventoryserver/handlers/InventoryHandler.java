package com.tailoredshapes.inventoryserver.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class InventoryHandler implements HttpHandler{
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response = "Hello, World";
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(response.getBytes());
        responseBody.close();
    }
}
