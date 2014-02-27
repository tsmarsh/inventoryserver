package com.tailoredshapes.inventoryserver.dao;

public interface Serialiser<T> {
    public byte[] serialise(T object);
}

