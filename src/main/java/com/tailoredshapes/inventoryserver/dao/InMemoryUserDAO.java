package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.utils.Algorithm;
import com.tailoredshapes.inventoryserver.utils.KeyProvider;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

public class InMemoryUserDAO<R extends Algorithm> implements UserDAO {
    private final Serialiser<User> serialiser;
    private final Encoder encoder;
    private KeyProvider<R> keyProvider;
    private final Map<Long, User> db = new HashMap<>();
    private UserDAO dao;

    @Inject
    public InMemoryUserDAO(Serialiser<User> serialiser, Encoder<R> encoder, KeyProvider<R> provider) {
        this.serialiser = serialiser;
        this.encoder = encoder;
        keyProvider = provider;
    }

    public User create(User user) {
        KeyPair keyPair = keyProvider.generate();

        user.setPublicKey(keyPair.getPublic()).setPrivateKey(keyPair.getPrivate());

        byte[] serialize = serialiser.serialise(user);

        Long id = encoder.encode(user, serialize);
        user.setId(id);

        db.put(id, user);
        return user;
    }

    public User read(User user) {
        return db.get(user.getId());
    }

    public User update(User user) {
        db.put(user.getId(), user);
        return user;
    }

    public User delete(User user) {
        User user1 = db.get(user.getId());
        db.remove(user.getId());
        return user1;
    }
}
