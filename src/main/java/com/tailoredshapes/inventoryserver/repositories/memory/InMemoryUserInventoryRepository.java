package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

import java.util.Collection;
import java.util.Map;

public class InMemoryUserInventoryRepository extends InMemoryNestedRepository<Inventory, Map<Long, Inventory>>{
    private Provider<User> parent;

    @Inject
    public InMemoryUserInventoryRepository(Map<Long, Inventory> db, DAO<Inventory> dao, Provider<User> parent) {
        super(db, dao);
        this.parent = parent;
    }

    @Override
    public Collection<Inventory> list() {
        return parent.get().getInventories();
    }
}
