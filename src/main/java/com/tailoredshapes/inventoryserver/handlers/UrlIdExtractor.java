package com.tailoredshapes.inventoryserver.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlIdExtractor implements IdExtractor {

    private final Pattern userIdPattern = Pattern.compile("^/(-?\\d+)/.*$");

    @Override
    public Long extract(HttpExchange exchange) {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        Matcher matcher = userIdPattern.matcher(path);
        if (matcher.matches()) {
            return Long.parseLong(matcher.group(1));
        }
        return null;

    }
}
