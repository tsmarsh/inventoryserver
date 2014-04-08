package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.security.KeyProvider;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

public class UserSaver<R> extends Saver<User> {

    private final KeyProvider<R> keyProvider;
    private final DAO<Inventory> inventoryDAO;

    @Inject
    public UserSaver(KeyProvider<R> keyProvider, DAO<Inventory> inventoryDAO) {
        this.keyProvider = keyProvider;
        this.inventoryDAO = inventoryDAO;
    }

    @Override
    public User saveChildren(User object) {
        if (object.getId() == null) {
            KeyPair keys = keyProvider.generate();
            object.setPrivateKey(keys.getPrivate());
            object.setPublicKey(keys.getPublic());
        }

        List<Inventory> savedInventories = new ArrayList<>();
        for (Inventory inventory : object.getInventories()) {
            savedInventories.add(upsert(inventory, inventoryDAO));
        }

        object.setInventories(savedInventories);
        return object;
    }
}
