package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

public class InMemoryUserInventoryRepository extends InMemoryNestedRepository<Inventory, User> {
    private Provider<User> parent;

    @Inject
    public InMemoryUserInventoryRepository(Map<Long, Inventory> db,
                                           DAO<Inventory> dao,
                                           Provider<User> parent,
                                           Repository<User, ?> parentRepo,
                                           Predicate<Inventory> filter) {
        super(db, dao, parent, filter, parentRepo);
        this.parent = parent;
    }

    @Override
    public Collection<Inventory> list() {
        return parent.get().getInventories();
    }
}
