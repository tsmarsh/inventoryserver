package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.function.Predicate;

public class HibernateUserInventoryRepository extends HibernateNestedRepository<Inventory, User> {
    private final Provider<User> parent;

    @Inject
    public HibernateUserInventoryRepository(EntityManager manager,
                                            TypeLiteral<Inventory> type,
                                            Provider<User> parent,
                                            DAO<Inventory> dao,
                                            Repository<User, ?> parentRepo,
                                            Predicate<Inventory> filter) {
        super(manager, type, parent, dao, parentRepo, filter);
        this.parent = parent;
    }

    @Override
    public Collection<Inventory> list() {
        return parent.get().getInventories();
    }
}
