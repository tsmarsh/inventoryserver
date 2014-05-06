package com.tailoredshapes.inventoryserver.modules;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.tailoredshapes.inventoryserver.InventoryServletConfig;
import com.tailoredshapes.inventoryserver.UserRootUrlBuilders;

public class UserRootHibernateServletConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return InventoryServletConfig.injector.createChildInjector(
                new InventoryServerModule("localhost", 7777),
                new HibernateModule(),
                new JSONModule(),
                new EncoderModule(),
                new UserRootUrlBuilders(),
                new UserRootServletModule(true, true),
                new UserRootHibernateRepositoryModule());
    }
}
