package com.tailoredshapes.inventoryserver.dao.hibernate;

import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.Saver;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.security.Algorithm;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;

public class HibernateDAO<T extends Cloneable & Idable<T>, R extends Algorithm> implements DAO<T> {
    private final Class<? super T> rawType;
    private EntityManager manager;
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

        System.out.println("Creating: " + object.toString());
        manager.persist(object);

        return object;
    }

    @Override
    public T read(T object) {

        return (T) manager.find(rawType, object.getId());
    }

    @Override
    public T update(T object) {
        T clone = cloneObjectForUpdate(object);
        clone = saver.saveChildren(clone);

        Long sig = encoder.encode(clone);
        clone.setId(sig);

        System.out.println("Updating: " + clone.toString());
        manager.persist(clone);
        return clone;
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
