package com.tailoredshapes.inventoryserver.urlbuilders;

@FunctionalInterface
public interface UrlBuilder<T> {
    String build(T t);
}

