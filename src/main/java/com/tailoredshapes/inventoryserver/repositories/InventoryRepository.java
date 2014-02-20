package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.model.Inventory;

/**
 * Created by tmarsh on 2/17/14.
 */
public interface InventoryRepository {
    Inventory findById(long id);
}
