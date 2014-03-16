package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Inject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.handlers.responders.Responder;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;


public class UserHandler implements HttpHandler {

    private final DAO<User> dao;
    private final Responder<User> responder;
    private final UrlBuilder<User> urlBuilder;
    private final UserParser userParser;
    private IdExtractor<User> extractor;

    @Inject
    public UserHandler(DAO<User> dao, Responder<User> responder, UrlBuilder<User> urlBuilder, UserParser userParser, IdExtractor<User> extractor) {
        this.dao = dao;
        this.responder = responder;
        this.urlBuilder = urlBuilder;
        this.userParser = userParser;
        this.extractor = extractor;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;
        User user;


        try (OutputStream responseBody = httpExchange.getResponseBody()) {
            switch (HttpMethod.valueOf(httpExchange.getRequestMethod())) {
                case get:
                    user = new User().setId(extractor.extract(httpExchange));
                    user = dao.read(user);
                    response = responder.respond(user, responseBody);
                    httpExchange.sendResponseHeaders(200, response.length());
                    break;
                case post:

                    @SuppressWarnings("unchecked")
                    Map<String, Object> parameters = (Map<String, Object>) httpExchange.getAttribute("parameters");
                    String jsonString = (String) parameters.get("user");

                    user = userParser.parse(jsonString);

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
