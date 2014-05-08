package com.tailoredshapes.inventoryserver.modules.jpa;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.tailoredshapes.inventoryserver.InventoryServletConfig;
import com.tailoredshapes.inventoryserver.modules.RoutesModule;
import com.tailoredshapes.inventoryserver.modules.InventoryServerModule;

public class HibernateServletConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return InventoryServletConfig.injector.createChildInjector(
                new InventoryServerModule("localhost", 7777),
                new RoutesModule(true, true),
                new UserRootHibernateModule(),
                new InventoryRootHibernateModule());
    }
}
