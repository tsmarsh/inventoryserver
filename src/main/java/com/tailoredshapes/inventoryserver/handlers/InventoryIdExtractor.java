package com.tailoredshapes.inventoryserver.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.model.Inventory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InventoryIdExtractor implements IdExtractor<Inventory>{

    private final Pattern userIdPattern = Pattern.compile("^/-?\\d+/inventory/(-?\\d+)");

    @Override
    public Long extract(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        Matcher matcher = userIdPattern.matcher(path);
        if(matcher.matches()){
            return Long.parseLong(matcher.group(1));
        }
        throw new RuntimeException("Invalid URL: " + path);
    }
}
