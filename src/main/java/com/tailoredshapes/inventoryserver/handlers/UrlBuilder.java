package com.tailoredshapes.inventoryserver.handlers;

public interface UrlBuilder<T> {
    String build(T t);
}

