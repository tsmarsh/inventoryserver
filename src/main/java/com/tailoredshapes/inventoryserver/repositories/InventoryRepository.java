package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

/**
 * Created by tmarsh on 2/17/14.
 */
public interface InventoryRepository {
    Inventory findById(User user, Long id);
}

