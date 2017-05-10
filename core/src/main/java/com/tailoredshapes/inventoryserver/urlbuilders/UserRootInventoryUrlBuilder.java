package com.tailoredshapes.inventoryserver.urlbuilders;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

public class UserRootInventoryUrlBuilder implements UrlBuilder<Inventory> {

  private final User currentUser;
  private final String protocol;
  private final String host;
  private final Integer port;

  public UserRootInventoryUrlBuilder(User currentUser,
                                     String protocol,
                                     String host,
                                     Integer port) {
    this.currentUser = currentUser;

    this.protocol = protocol;
    this.host = host;
    this.port = port;
  }

  @Override
  public String build(Inventory inventory) {
    return inventory.getId() != null ?
           String.format("%s://%s:%s/users/%s/%s/inventories/%s",
                         protocol,
                         host,
                         port,
                         currentUser.getName(),
                         currentUser.getId(),
                         inventory.getId())
                                     : null;

  }
}

