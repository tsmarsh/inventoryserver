package com.tailoredshapes.inventoryserver.model.builders;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

public class UserBuilder {

  private final User user;
  Long id = 555l;
  String name = "Archer";
  Date created = new Date();
  Collection<Inventory> inventoryMap = new HashSet<>();

  public UserBuilder() {
    user = new User();
  }

  public UserBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public UserBuilder name(String name) {
    this.name = name;
    return this;
  }

  public UserBuilder inventories(Collection inventoryMap) {
    this.inventoryMap = inventoryMap;
    return this;
  }

  public UserBuilder created(Date created){
    this.created = created;
    return this;
  }

  public User build() {
    return user.setId(id).setCreated(created).setName(name).setInventories(inventoryMap);
  }
}
