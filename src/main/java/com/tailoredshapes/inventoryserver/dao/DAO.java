package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.User;

public interface DAO<T> {
    T create(T object);

    T read(T object);

    T update(T object);

    T delete(T object);
}

