package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Inject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.tailoredshapes.inventoryserver.dao.UserDAO;
import com.tailoredshapes.inventoryserver.model.User;

import java.io.IOException;
import java.io.OutputStream;


public class UserHandler implements HttpHandler {

    private final UserDAO dao;
    private final Responder<User> responder;

    @Inject
    public UserHandler(UserDAO dao, Responder<User> responder) {
        this.dao = dao;
        this.responder = responder;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        User user = null;
        String response;

        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            switch (HttpMethod.valueOf(httpExchange.getRequestMethod())) {
                case put:
                    user = dao.create(user);
                    response = responder.respond(user, responseBody);
                    httpExchange.sendResponseHeaders(200, response.length());
                    break;
                case get:
                    user = dao.read(user);
                    response = responder.respond(user, responseBody);
                    httpExchange.sendResponseHeaders(200, response.length());
                    break;
                case post:
                    user = dao.update(user);
                    response = responder.respond(user, responseBody);
                    httpExchange.sendResponseHeaders(304, response.length());
                    break;
                case delete:
                    user = dao.delete(user);
                    response = responder.respond(user, responseBody);
                    httpExchange.sendResponseHeaders(200, response.length());
                    break;
            }

        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        }
    }
}
