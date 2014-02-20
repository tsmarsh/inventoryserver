package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

import java.io.InputStream;

/**
 * Created by tmarsh on 2/16/14.
 */
public interface DAO<T> {
    T create(User user, T object);

    T read(User user, T object);

    T update(User user, T object);

    T delete(User user, T object);
}
