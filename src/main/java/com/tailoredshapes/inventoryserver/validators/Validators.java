package com.tailoredshapes.inventoryserver.validators;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

public interface Validators {
  Validator<Inventory> inventoryValidator = (inventory) -> inventory.getCategory() != null;
  Validator<User> userValidator = (user) -> user.getName() != null;
}
