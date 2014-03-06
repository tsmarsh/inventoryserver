package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Inject;
import com.sun.net.httpserver.Headers;
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
    private UrlBuilder<Inventory> urlBuilder;
    private IdExtractor inventoryUrlExtractor;

    @Inject
    public InventoryHandler(Responder<Inventory> responder,
                            Authenticator authenticator,
                            DAO<Inventory> dao,
                            Parser<Inventory> parser,
                            UrlBuilder<Inventory> urlBuilder,
                            IdExtractor<Inventory> inventoryUrlExtractor
    ) {
        this.responder = responder;
        this.authenticator = authenticator;
        this.dao = dao;
        inventoryParser = parser;
        this.urlBuilder = urlBuilder;
        this.inventoryUrlExtractor = inventoryUrlExtractor;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;

        Inventory inventory;

        try (OutputStream responseBody = httpExchange.getResponseBody()) {

            switch (HttpMethod.valueOf(httpExchange.getRequestMethod())) {
                case get:
                    inventory = new Inventory().setId(inventoryUrlExtractor.extract(httpExchange));
                    inventory = dao.read(inventory);
                    response = responder.respond(inventory, responseBody);
                    httpExchange.sendResponseHeaders(200, response.length());
                    break;
                case post:
                    @SuppressWarnings("unchecked")
                    Map<String, Object> parameters = (Map<String, Object>) httpExchange.getAttribute("parameters");
                    String jsonString = (String) parameters.get("inventory");

                    inventory = inventoryParser.parse(jsonString);

                    if (inventory.getId() == null) {
                        inventory = dao.create(inventory);
                    } else {
                        inventory = dao.update(inventory);
                    }

                    response = responder.respond(inventory, responseBody);
                    Headers responseHeaders = httpExchange.getResponseHeaders();
                    responseHeaders.add("location", urlBuilder.build(inventory));
                    httpExchange.sendResponseHeaders(302, response.length());
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
            httpExchange.sendResponseHeaders(500, 0);
        }
    }
}
