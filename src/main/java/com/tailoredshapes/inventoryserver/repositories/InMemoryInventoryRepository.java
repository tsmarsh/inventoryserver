package com.tailoredshapes.inventoryserver.repositories;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

public class InMemoryInventoryRepository implements InventoryRepository {

    private DAO<Inventory> dao;

    @Inject
    public InMemoryInventoryRepository(InMemoryDAO<Inventory> inventoryDAO) {

        this.dao = inventoryDAO;
    }

    @Override
    public Inventory findById(User user, Long id) {
        return dao.read(user, new Inventory().setId(id));
    }
}
