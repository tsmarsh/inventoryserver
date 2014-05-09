package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.TypeLiteral;
import com.google.inject.servlet.RequestScoped;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

@RequestScoped
public class HibernateRepository<T extends Idable<T>> implements Repository<T, EntityManager> {
    private final EntityManager manager;
    private final DAO<T> dao;
    private final Class<T> rawType;

    @Inject
    public HibernateRepository(EntityManager manager, TypeLiteral<T> type, DAO<T> dao) {
        this.manager = manager;
        this.dao = dao;
        //noinspection unchecked,unchecked
        this.rawType = (Class<T>) type.getRawType();
    }

    @Override
    public T findById(Long id) {
        return manager.find(rawType, id);
    }

    @Override
    public T findBy(Finder<T, EntityManager> finder) {
        return finder.find(manager);
    }

    @Override
    public Collection<T> listBy(Finder<Collection<T>, EntityManager> finder) {
        return finder.find(manager);
    }

    @Override
    public Collection<T> list() {
        return manager.createQuery(String.format("select t from %s t", rawType.getName())).getResultList();
    }

    @Override
    public T save(final T t) {
        return t.getId() == null ? dao.create(t) : dao.update(t);
    }
}
