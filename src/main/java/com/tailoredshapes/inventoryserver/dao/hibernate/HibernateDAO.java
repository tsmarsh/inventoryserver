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

public class HibernateDAO<T extends Idable<T>, R extends Algorithm> implements DAO<T> {
    private TypeLiteral<T> type;
    private final SessionFactory factory;
    private final Class<? super T> rawType;
    private final Saver<T> saver;
    private Encoder<T, R> encoder;

    @Inject
    public HibernateDAO(TypeLiteral<T> type, SessionFactory factory, Saver<T> saver, Encoder<T, R> encoder) {
        this.saver = saver;
        this.encoder = encoder;
        rawType = type.getRawType();
        this.factory = factory;
    }

    @Override
    public T create(T object) {
        Session currentSession = factory.getCurrentSession();
        object = saver.saveChildren(object);

        Long sig = encoder.encode(object);
        object.setId(sig);

        currentSession.save(object);
        return object;
    }

    @Override
    public T read(T object) {
        Session currentSession = factory.getCurrentSession();

        return (T) currentSession.get(rawType, object.getId());
    }

    @Override
    public T update(T object) {
        Session currentSession = factory.getCurrentSession();
        object = saver.saveChildren(object);

        Long sig = encoder.encode(object);
        object.setId(sig);

        currentSession.saveOrUpdate(object);
        return object;
    }

    @Override
    public T delete(T object) {
        Session currentSession = factory.getCurrentSession();
        object = (T) currentSession.get(rawType, object.getId());
        currentSession.delete(object);
        return object;
    }
}
