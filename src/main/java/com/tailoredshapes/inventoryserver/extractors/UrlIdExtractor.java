package com.tailoredshapes.inventoryserver.extractors;

import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.model.User;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlIdExtractor implements IdExtractor<User> {

    private final Pattern userIdPattern = Pattern.compile("/users/(-?\\d+)/?.*$");

    @Override
    public Long extract(HttpExchange exchange) {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        return extract(path);
    }

    @Override
    public Long extract(String path) {
        Matcher matcher = userIdPattern.matcher(path);
        if (matcher.matches()) {
            return Long.parseLong(matcher.group(1));
        } else {
            return null;
        }
    }
}
