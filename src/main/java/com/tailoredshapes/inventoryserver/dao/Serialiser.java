package com.tailoredshapes.inventoryserver.dao;

/**
 * Created by tmarsh on 2/17/14.
 */
public interface Serialiser<T> {
    public byte[] serialise(T object);
}
