package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;

import javax.persistence.EntityManager;

public class HibernateInventoryRepository implements InventoryRepository {


    private final EntityManager manager;

    @Inject
    public HibernateInventoryRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Inventory findById(Long id) {
        return manager.find(Inventory.class, id);
    }
}

