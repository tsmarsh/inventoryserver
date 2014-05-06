package com.tailoredshapes.inventoryserver.modules.jpa;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.tailoredshapes.inventoryserver.InventoryServletConfig;
import com.tailoredshapes.inventoryserver.UserRootUrlBuilders;
import com.tailoredshapes.inventoryserver.modules.EncoderModule;
import com.tailoredshapes.inventoryserver.modules.InventoryServerModule;
import com.tailoredshapes.inventoryserver.modules.JSONModule;
import com.tailoredshapes.inventoryserver.modules.UserRootServletModule;

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
