package com.tailoredshapes.inventoryserver.repositories.memory;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import javax.inject.Provider;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class InMemoryNestedRepository<T extends Idable<T>, P> extends InMemoryRepository<T> {
    private final DAO<T> dao;
    private final Provider<P> parent;
    private final Predicate<? super T> filter;
    private final Repository<P, ?> parentRepo;

    InMemoryNestedRepository(Map<Long, T> db,
                             DAO<T> dao,
                             Provider<P> parent,
                             Predicate<? super T> filter,
                             Repository<P, ?> parentRepo) {
        super(db, dao);
        this.dao = dao;
        this.parent = parent;
        this.filter = filter;
        this.parentRepo = parentRepo;
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
