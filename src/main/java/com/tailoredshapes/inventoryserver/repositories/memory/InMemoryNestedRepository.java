package com.tailoredshapes.inventoryserver.repositories.memory;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import javax.inject.Provider;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class InMemoryNestedRepository<T extends Idable<T>, P> extends InMemoryRepository<T>{
    private DAO<T> dao;
    private Provider<P> parent;
    private Predicate<? super T> filter;
    private Repository<P, ? > parentRepo;

    public InMemoryNestedRepository(Map<Long, T> db, DAO<T> dao) {
        super(db, dao);
    }

    @Override
    public T save(final T t) {
        T savedT = t.getId() == null ? dao.create(t) : dao.update(t);

        P parent = this.parent.get();
        Collection<T> ts = list();
        ts.removeIf(filter);

        ts.add(savedT);
        parentRepo.save(parent);
        return savedT;
    }
}
