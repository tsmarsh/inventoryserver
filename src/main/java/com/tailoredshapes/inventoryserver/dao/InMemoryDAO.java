package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.model.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDAO<T extends Idable<T>> implements DAO<T>{

    Map<User, Map<Long, T>> db;
    private Serialiser<T> serialiser;
    private Encoder encoder;

    protected InMemoryDAO(Serialiser<T> serialiser, Encoder encoder) {
        this.serialiser = serialiser;
        this.encoder = encoder;
        db = new HashMap<>();
    }

    private Map<Long, T> getUserMap(User user) {
        Map<Long, T> usermap = db.get(user);
        if(null == usermap){
            usermap = new HashMap<>();
            db.put(user, usermap);
        }

        return usermap;
    }

    @Override
    public T create(User user, T object) {
        Map<Long, T> usermap = getUserMap(user);
        byte[] bits = serialiser.serialise(object);

        Long sig = encoder.encode(user, bits);
        object.setId(sig);
        usermap.put(sig, object);
        return object;
    }

    @Override
    public T read(User user, T object) {
        Map<Long, T> usermap = getUserMap(user);
        byte[] bits = serialiser.serialise(object);

        Long sig = encoder.encode(user, bits);
        return usermap.get(sig);
    }

    @Override
    public T update(User user, T object) {
        Map<Long, T> usermap = getUserMap(user);
        if(!usermap.containsKey(object.getId())){
            throw new RuntimeException("Object does not exist");
        }
        byte[] bits = serialiser.serialise(object);
        Long sig = encoder.encode(user, bits);
        object.setId(sig);
        usermap.put(sig, object);
        return object;
    }

    @Override
    public T delete(User user, T object) {
        Map<Long, T> usermap = getUserMap(user);
        T oldObject = usermap.get(object.getId());
        usermap.remove(object.getId());
        return oldObject;
    }
}
