package com.tailoredshapes.inventoryserver.dao;

import java.util.ArrayList;
import java.util.List;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

public class UserSaver implements Saver<User> {
  private final DAO<Inventory> inventoryDAO;

  public UserSaver(DAO<Inventory> inventoryDAO) {
    this.inventoryDAO = inventoryDAO;
  }

  @Override
  public User saveChildren(DAO<User> dao, User object) {
    List<Inventory> savedInventories = new ArrayList<>();
    for (Inventory inventory : object.getInventories()) {
      savedInventories.add(inventoryDAO.upsert(inventory));
    }

    object.setInventories(savedInventories);
    return object;
  }
}
