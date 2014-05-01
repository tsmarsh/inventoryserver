package com.tailoredshapes.inventoryserver.repositories.memory;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;

public class InMemoryRepository<T extends Idable<T>> implements Repository<T, Map<Long, T>> {

    private final Map<Long, T> db;
    private final DAO<T> dao;

    @Inject
    public InMemoryRepository(Map<Long, T> db, DAO<T> dao) {
        this.db = db;
        this.dao = dao;
    }

    @Override
    public T findById(Long extract) {
        return db.get(extract);
    }

    @Override
    public T findBy(Finder<T, Map<Long, T>> finder) {
        return finder.find(db);
    }

    @Override
    public Collection<T> listBy(Finder<Collection<T>, Map<Long, T>> finder) {
        return finder.find(db);
    }

    @Override
    public Collection<T> list() {
        return db.values();
    }

    @Override
    public T save(T t) {
        return t.getId() == null ? dao.create(t) : dao.update(t);
    }
}
