package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.utils.Algorithm;

import java.util.HashMap;
import java.util.Map;

public abstract class InMemoryDAO<T extends Idable<T>, R extends Algorithm> implements DAO<T> {

    public final Map<Long, T> db;
    private final Encoder<T, R> encoder;

    public abstract T saveChildren(T object);

    protected <Z extends Idable<Z>> void upsert(Z object, DAO<Z> dao){
        if(object != null){
            if(null == object.getId()){
                dao.create(object);
            } else{
                dao.update(object);
            }
        }
    }

    @Inject
    public InMemoryDAO(Encoder<T, R> encoder) {
        this.encoder = encoder;
        db = new HashMap<>();
    }

    @Override
    public T create(T object) {
        object = saveChildren(object);

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
        object = saveChildren(object);
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

