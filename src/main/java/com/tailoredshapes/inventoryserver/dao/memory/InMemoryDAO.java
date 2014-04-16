package com.tailoredshapes.inventoryserver.dao.memory;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.Saver;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.security.Algorithm;

import javax.inject.Inject;
import java.util.Map;

public class InMemoryDAO<T extends Idable<T>, R extends Algorithm> implements DAO<T> {

    private final Map<Long, T> db;
    private final Encoder<T, R> encoder;
    private final Saver<T> saver;

    @Inject
    public InMemoryDAO(Map<Long, T> db, Encoder<T, R> encoder, Saver<T> saver) {
        this.encoder = encoder;
        this.saver = saver;
        this.db = db;
    }

    @Override
    public T create(T object) {
        object = saver.saveChildren(object);

        Long sig = encoder.encode(object);
        object.setId(sig);
        db.put(sig, object);
        return object;
    }

    @Override
    public T read(T object) {
        return db.get(object.getId());
    }

    @Override
    public T update(T object) {

        if (!db.containsKey(object.getId())) {
            throw new RuntimeException("Object does not exist");
        }
        object = saver.saveChildren(object);
        Long sig = encoder.encode(object);
        object.setId(sig);
        db.put(sig, object);
        return object;
    }

    @Override
    public T delete(T object) {
        object = db.get(object.getId());
        db.remove(object.getId());
        return object;
    }
}

