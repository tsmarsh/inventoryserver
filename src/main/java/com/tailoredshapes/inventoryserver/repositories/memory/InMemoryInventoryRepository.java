package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;

public class InMemoryInventoryRepository implements InventoryRepository {

    private final DAO<Inventory> dao;

    @Inject
    public InMemoryInventoryRepository(InMemoryDAO<Inventory> inventoryDAO) {

        this.dao = inventoryDAO;
    }

    @Override
    public Inventory findById(User user, Long id) {
        return dao.read(user, new Inventory().setId(id));
    }
}
