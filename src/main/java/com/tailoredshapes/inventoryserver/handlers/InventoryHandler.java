package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Inject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.utils.Parser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


public class InventoryHandler implements HttpHandler {

    private final Responder<Inventory> responder;
    private final Authenticator authenticator;
    private final DAO<Inventory> dao;
    private final Parser<Inventory> inventoryParser;

    @Inject
    public InventoryHandler(Responder<Inventory> responder,
                            Authenticator authenticator,
                            DAO<Inventory> dao,
                            Parser<Inventory> parser) {
        this.responder = responder;
        this.authenticator = authenticator;
        this.dao = dao;
        inventoryParser = parser;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        User user = authenticator.authenticate(httpExchange);

        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) httpExchange.getAttribute("parameters");
        String jsonString = (String) parameters.get("inventory");

        Inventory inventory = inventoryParser.parse(jsonString);
        String response;

        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            switch (HttpMethod.valueOf(httpExchange.getRequestMethod())) {
                case get:
                    inventory = dao.read(user, inventory);
                    response = responder.respond(inventory, responseBody);
                    httpExchange.sendResponseHeaders(200, response.length());
                    break;
                case post:
                    inventory = dao.update(user, inventory);
                    response = responder.respond(inventory, responseBody);
                    httpExchange.sendResponseHeaders(304, response.length());
                    break;
            }

        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        }
    }
}
