package com.tailoredshapes.inventoryserver.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryIdExtractor implements IdExtractor{

    private final Pattern userIdPattern = Pattern.compile("^/-?\\d+/inventories/(-?\\d+)");

    @Override
    public Long extract(HttpExchange exchange) {
        Matcher matcher = userIdPattern.matcher(exchange.getRequestURI().getPath());
        if(matcher.matches()){
            return Long.parseLong(matcher.group(1));
        }
        throw new RuntimeException("Invalid URL");
    }
}
