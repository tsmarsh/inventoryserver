package com.tailoredshapes.inventoryserver.dao.hibernate;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.persist.Transactional;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.Saver;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.security.Algorithm;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;

public class HibernateDAO<T extends Cloneable & Idable<T>, R extends Algorithm> implements DAO<T> {
    private final Class<? super T> rawType;
    private final EntityManager manager;
    private final Saver<T> saver;
    private final Encoder<T, R> encoder;

    @Inject
    public HibernateDAO(TypeLiteral<T> type, EntityManager manager, Saver<T> saver, Encoder<T, R> encoder) {
        this.manager = manager;
        this.saver = saver;
        this.encoder = encoder;
        rawType = type.getRawType();
    }

    @Override
    public T create(T object) {
        object = saver.saveChildren(object);

        Long sig = encoder.encode(object);

        object.setId(sig);
        T read = read(object);
        if (read == null) {
            manager.persist(object);
            manager.flush();
            return object;
        } else {
            return read;
        }
    }

    @Override
    public T read(T object) {
        return (T) manager.find(rawType, object.getId());
    }

    @Override
    public T update(T object) {
        T result;
        T clone = cloneObjectForUpdate(object);
        clone = saver.saveChildren(clone);

        Long sig = encoder.encode(clone);
        if (!sig.equals(object.getId())) {
            clone.setId(sig);
            T read = read(clone);
            if (read == null) {
                manager.persist(clone);
                try {
                    manager.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result = clone;
            } else {
                result = read;
            }
        } else {
            result = object;
        }
        return result;
    }

    @Override
    public T delete(T object) {
        object = (T) manager.find(rawType, object.getId());
        manager.remove(object);
        return object;
    }

    private T cloneObjectForUpdate(T object) {
        T clone = null;
        try {
            clone = (T) object.getClass().getMethod("clone").invoke(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
