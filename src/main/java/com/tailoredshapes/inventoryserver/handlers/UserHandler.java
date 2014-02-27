package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Inject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.tailoredshapes.inventoryserver.dao.UserDAO;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.utils.UserParser;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


public class UserHandler implements HttpHandler {

    private final UserDAO dao;
    private final Responder<User> responder;
    private UrlBuilder<User> urlBuilder;
    private UserParser userParser;

    @Inject
    public UserHandler(UserDAO dao, Responder<User> responder, UrlBuilder<User> urlBuilder, UserParser userParser) {
        this.dao = dao;
        this.responder = responder;
        this.urlBuilder = urlBuilder;
        this.userParser = userParser;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;

        @SuppressWarnings("unchecked")
        Map<String, Object> parameters = (Map<String, Object>) httpExchange.getAttribute("parameters");
        String jsonString = (String) parameters.get("user");

        User user = userParser.parse(jsonString);

        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            switch (HttpMethod.valueOf(httpExchange.getRequestMethod())) {
                case get:
                    user = dao.read(user);
                    response = responder.respond(user, responseBody);
                    httpExchange.sendResponseHeaders(200, response.length());
                    break;
                case post:
                    if (null == user.getId()) {
                        user = dao.create(user);
                    } else {
                        user = dao.update(user);
                    }
                    response = responder.respond(user, responseBody);
                    Headers responseHeaders = httpExchange.getResponseHeaders();
                    responseHeaders.add("location", urlBuilder.build(user));
                    httpExchange.sendResponseHeaders(302, response.length());
                    break;
                default:
                    throw new RuntimeException("Method not supported.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            httpExchange.sendResponseHeaders(500, 0);
        }
    }
}
