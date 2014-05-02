package com.tailoredshapes.inventoryserver.urlbuilders;

import com.tailoredshapes.inventoryserver.model.Inventory;

import javax.inject.Inject;
import javax.inject.Named;

public class InventoryUrlBuilder implements UrlBuilder<Inventory> {

    private final String protocol;
    private final String host;
    private final Integer port;

    @Inject
    public InventoryUrlBuilder(@Named("protocol") String protocol,
                               @Named("host") String host,
                               @Named("port") Integer port) {


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
