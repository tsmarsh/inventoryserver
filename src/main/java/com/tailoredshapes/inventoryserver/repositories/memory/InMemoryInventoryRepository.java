package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;
import com.tailoredshapes.inventoryserver.security.Algorithm;

public class InMemoryInventoryRepository implements InventoryRepository {

    private final DAO<Inventory> dao;

    @Inject
    public InMemoryInventoryRepository(DAO<Inventory> inventoryDAO) {

        this.dao = inventoryDAO;
    }

    @Override
    public Inventory findById(Long id) {
        return dao.read(new Inventory().setId(id));
    }
}
