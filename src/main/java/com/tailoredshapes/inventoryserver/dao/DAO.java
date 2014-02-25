package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.User;

public interface DAO<T> {
    T create(User user, T object);

    T read(User user, T object);

    T update(User user, T object);

    T delete(User user, T object);
}
