package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;
import com.tailoredshapes.inventoryserver.security.Algorithm;

public class InMemoryInventoryRepository<R extends Algorithm> implements InventoryRepository {

    private final DAO<Inventory> dao;

    @Inject
    public InMemoryInventoryRepository(InMemoryDAO<Inventory, R> inventoryDAO) {

        this.dao = inventoryDAO;
    }

    @Override
    public Inventory findById(User user, Long id) {
        return dao.read(new Inventory().setId(id));
    }
}
