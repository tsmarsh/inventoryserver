package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Inject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.tailoredshapes.inventoryserver.dao.UserDAO;
import com.tailoredshapes.inventoryserver.model.User;

import java.io.IOException;
import java.io.OutputStream;


public class UserHandler implements HttpHandler {

    private final UserDAO dao;
    private final Responder<User> responder;
    private UrlBuilder<User> urlBuilder;

    @Inject
    public UserHandler(UserDAO dao, Responder<User> responder, UrlBuilder<User> urlBuilder) {
        this.dao = dao;
        this.responder = responder;
        this.urlBuilder = urlBuilder;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        User user = null;
        String response;

        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            switch (HttpMethod.valueOf(httpExchange.getRequestMethod())) {
                case get:
                    user = dao.read(user);
                    response = responder.respond(user, responseBody);
                    httpExchange.sendResponseHeaders(200, response.length());
                    break;
                case post:
                    user = dao.update(user);
                    response = responder.respond(user, responseBody);
                    Headers responseHeaders = httpExchange.getResponseHeaders();
                    responseHeaders.add("location", urlBuilder.build(user));
                    httpExchange.sendResponseHeaders(302, response.length());
                    break;
                default:
                    throw new RuntimeException("Method not supported.");
            }

        } catch (Exception e) {
            httpExchange.sendResponseHeaders(500, 0);
        }
    }
}
