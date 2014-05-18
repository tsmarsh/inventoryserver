package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.tailoredshapes.inventoryserver.modules.InventoryServerModule;
import com.tailoredshapes.inventoryserver.modules.RoutesModule;
import com.tailoredshapes.inventoryserver.modules.jpa.InventoryRootJPAModule;
import com.tailoredshapes.inventoryserver.modules.jpa.UserRootJPAModule;

public class HibernateServletConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new JpaPersistModule("inventory_server"),
                new InventoryServerModule("localhost", 7777),
                new RoutesModule(true, true),
                new UserRootJPAModule(),
                new InventoryRootJPAModule());
    }
}

