package com.tailoredshapes.inventoryserver.dao.hibernate;

import com.google.inject.TypeLiteral;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.Saver;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.security.Algorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unchecked")
public class HibernateDAO<T extends Cloneable & Idable<T>, R extends Algorithm> implements DAO<T> {
    private final Class<? super T> rawType;
    private final EntityManager manager;
    private final Saver<T> saver;
    private final Encoder<T, R> encoder;

    private final Logger log = LoggerFactory.getLogger(HibernateDAO.class);
    @Inject
    public HibernateDAO(TypeLiteral<T> type, EntityManager manager, Saver<T> saver, Encoder<T, R> encoder) {
        this.manager = manager;
        this.saver = saver;
        this.encoder = encoder;
        rawType = type.getRawType();
    }

    @Override
    public T create(T object) {
        log.info("Creating: " + object);
        object = saver.saveChildren(object);

        Long sig = encoder.encode(object);

        object.setId(sig);
        T read = read(object);
        T out;
        if (read == null) {
            log.info("Saving new");
            manager.persist(object);
            manager.flush();
            out = object;
        } else {
            log.info("Found existing");
            out = read;
        }

        log.info("Created: " + out);
        return out;
    }

    @Override
    public T read(T object) {
        return (T) manager.find(rawType, object.getId());
    }

    @Override
    public T update(T object) {
        log.info(">> Updating: " + object);
        T clone = cloneObjectForUpdate(object);
        clone = saver.saveChildren(clone);

        Long sig = encoder.encode(clone);
        T out;
        if (!sig.equals(object.getId())) {
            clone.setId(sig);
            T read = read(clone);
            if (read == null) {
                log.info("Saving clone: " + clone);
                manager.persist(clone);
                try {
                    manager.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                out = clone;
            } else {
                log.info("Already exists: " + read);
                out = read;
            }
        } else {
            out = object;
        }

        log.info("<< Updated: " +  out);
        return out;
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
