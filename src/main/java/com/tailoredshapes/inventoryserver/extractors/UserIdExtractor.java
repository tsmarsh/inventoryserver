package com.tailoredshapes.inventoryserver.extractors;

import com.google.inject.servlet.RequestScoped;
import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.model.User;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class UserIdExtractor implements IdExtractor<Long, User> {

    private final Pattern userIdPattern = Pattern.compile("/users/\\w+/?(-?\\d+)?/?.*$");

    @Override
    public Long extract(HttpExchange exchange) {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        return extract(path);
    }

    @Override
    public Long extract(String path) {
        Matcher matcher = userIdPattern.matcher(path);
        Long id = null;
        if (matcher.matches()) {
            id = Long.parseLong(matcher.group(1));
        }

        return id;
    }
}

