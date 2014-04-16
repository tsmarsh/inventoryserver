package com.tailoredshapes.inventoryserver.repositories.memory;

import javax.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;

import java.util.Collection;
import java.util.Map;

public class InMemoryInventoryRepository implements InventoryRepository {

    private final DAO<Inventory> dao;
    private Map<Long, Inventory> db;

    @Inject
    public InMemoryInventoryRepository(DAO<Inventory> inventoryDAO, Map<Long, Inventory> db) {

        this.dao = inventoryDAO;
        this.db = db;
    }

    @Override
    public Inventory findById(Long id) {
        return dao.read(new Inventory().setId(id));
    }

    @Override
    public Collection<Inventory> list() {
        return db.values();
    }
}
