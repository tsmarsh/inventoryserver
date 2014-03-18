package com.tailoredshapes.inventoryserver.urlbuilders;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

public class InventoryUrlBuilder implements UrlBuilder<Inventory> {

    private final User currentUser;
    private final String protocol;
    private final String host;
    private final Integer port;

    @Inject
    public InventoryUrlBuilder(User currentUser, String protocol, String host, Integer port) {
        this.currentUser = currentUser;

        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    @Override
    public String build(Inventory inventory) {
        return String.format("%s://%s:%s/users/%s/inventories/%s", protocol, host, port, currentUser.getId(), inventory.getId());
    }
}
