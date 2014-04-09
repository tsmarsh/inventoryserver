package com.tailoredshapes.inventoryserver.validators;

import com.tailoredshapes.inventoryserver.model.Inventory;

public class InventoryValidator implements Validator<Inventory> {

    @Override
    public Boolean validate(Inventory user) {
        return user.getCategory() != null;
    }
}
