package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.model.Inventory;

public interface InventoryRepository {
    Inventory findById(Long id);
}

