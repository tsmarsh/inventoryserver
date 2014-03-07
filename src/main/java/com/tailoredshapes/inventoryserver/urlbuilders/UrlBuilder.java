package com.tailoredshapes.inventoryserver.urlbuilders;

public interface UrlBuilder<T> {
    String build(T t);
}

