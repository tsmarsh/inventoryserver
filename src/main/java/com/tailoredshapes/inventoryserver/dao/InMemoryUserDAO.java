package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.utils.Algorithm;
import com.tailoredshapes.inventoryserver.utils.KeyProvider;

import java.security.KeyPair;

public class InMemoryUserDAO<R extends Algorithm> extends InMemoryDAO<User, R> {

    private KeyProvider<R> keyProvider;

    @Inject
    public InMemoryUserDAO(Encoder<User, R> encoder, KeyProvider<R> keyProvider) {
        super(encoder);
        this.keyProvider = keyProvider;
    }

    @Override
    public User saveChildren(User object) {
        if(object.getId() == null){
            KeyPair keys = keyProvider.generate();
            object.setPrivateKey(keys.getPrivate());
            object.setPublicKey(keys.getPublic());
        }
        return object;
    }
}
