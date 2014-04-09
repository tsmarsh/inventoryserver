package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;

import javax.persistence.EntityManager;
import java.util.Collection;

public class HibernateInventoryRepository implements InventoryRepository {


    private final EntityManager manager;
    private final Provider<User> currentUser;

    @Inject
    public HibernateInventoryRepository(EntityManager manager, @Named("current_user") Provider<User> currentUser) {
        this.manager = manager;
        this.currentUser = currentUser;
    }

    @Override
    public Inventory findById(Long id) {
        return manager.find(Inventory.class, id);
    }

    @Override
    public Collection<Inventory> list() {
        return currentUser.get().getInventories();
    }
}

