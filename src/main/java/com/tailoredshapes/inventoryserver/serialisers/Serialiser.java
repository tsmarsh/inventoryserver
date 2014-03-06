package com.tailoredshapes.inventoryserver.serialisers;

public interface Serialiser<T> {
    public byte[] serialise(T object);
}

