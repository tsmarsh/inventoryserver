package com.tailoredshapes.inventoryserver.serialisers;

public interface Serialiser<T> {
    String serialise(T object);
}

