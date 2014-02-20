package com.tailoredshapes.inventoryserver.dao;

/**
 * Created by tmarsh on 2/17/14.
 */
public interface Serializer<T> {
    public byte[] serialize(T object);
}
