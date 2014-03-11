package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.security.KeyProvider;

import java.security.KeyPair;

public class UserSaver<R> extends Saver<User> {

    private KeyProvider<R> keyProvider;

    @Inject
    public UserSaver(KeyProvider<R> keyProvider) {
        this.keyProvider = keyProvider;
    }

    @Override
    public User saveChildren(User object) {
        if (object.getId() == null) {
            KeyPair keys = keyProvider.generate();
            object.setPrivateKey(keys.getPrivate());
            object.setPublicKey(keys.getPublic());
        }
        return object;
    }
}
