package com.tailoredshapes.inventoryserver;

import com.google.inject.Key;
import com.google.inject.servlet.ServletModule;
import com.tailoredshapes.inventoryserver.filters.TFilter;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.servlets.Pestlet;

public class InventoryServletModule extends ServletModule {
    @Override
    protected void configureServlets() {
        serveRegex("/users/?-?\\d+/inventories[/-?\\d+]?").with(new Key<Pestlet<Inventory>>() {});
        filterRegex("/users/?-?\\d+/inventories[/-?\\d+]?").through(new Key<TFilter<Inventory>>(){});

        serveRegex("/users(/-?\\d+)?$").with(new Key<Pestlet<User>>(){});
        filterRegex("/users(/-?\\d+)?$").through(new Key<TFilter<User>>() {});
    }
}
