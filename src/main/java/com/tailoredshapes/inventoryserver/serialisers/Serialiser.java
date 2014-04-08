package com.tailoredshapes.inventoryserver.serialisers;

public interface Serialiser<T, Z> {
    public Z serialise(T object);
}

