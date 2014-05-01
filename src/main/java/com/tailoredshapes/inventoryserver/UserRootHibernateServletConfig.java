package com.tailoredshapes.inventoryserver;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class UserRootHibernateServletConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return InventoryServletConfig.injector.createChildInjector(
                new InventoryServerModule("localhost", 7777),
                new HibernateModule(),
                new JSONModule(),
                new EncoderModule(),
                new UserRootServletModule(true, true),
                new UserRootHibernateRepositoryModule());
    }
}
