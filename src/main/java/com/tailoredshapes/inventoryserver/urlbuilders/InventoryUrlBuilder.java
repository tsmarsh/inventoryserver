package com.tailoredshapes.inventoryserver.urlbuilders;

import com.tailoredshapes.inventoryserver.model.Inventory;

public class InventoryUrlBuilder implements UrlBuilder<Inventory> {

  private final String protocol;
  private final String host;
  private final Integer port;

  public InventoryUrlBuilder(String protocol,
                             String host,
                             Integer port) {

    this.protocol = protocol;
    this.host = host;
    this.port = port;
  }

  @Override
  public String build(Inventory inventory) {
    return inventory.getId() != null ?
           String.format("%s://%s:%s/inventories/%s", protocol, host, port, inventory.getId())
                                     : null;

  }
}
