package com.tailoredshapes.inventoryserver.handlers;

import com.sun.net.httpserver.HttpExchange;

public interface UserIdExtractor {
    Long extract(HttpExchange exchange);
}

