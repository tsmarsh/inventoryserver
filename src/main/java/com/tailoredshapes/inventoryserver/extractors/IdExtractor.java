package com.tailoredshapes.inventoryserver.extractors;

import com.sun.net.httpserver.HttpExchange;

public interface IdExtractor<T> {
    Long extract(HttpExchange exchange);

    Long extract(String path);
}

