package com.tailoredshapes.inventoryserver.handlers;

import com.sun.net.httpserver.HttpExchange;

public interface IdExtractor {
    Long extract(HttpExchange exchange);
}

