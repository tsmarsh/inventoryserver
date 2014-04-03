package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

public class InventoryServletConfig extends GuiceServletContextListener {

    public static Injector injector = Guice.createInjector(
            new JpaPersistModule("inventory_server"),
            new InventoryServerModule("localhost", 7777),
            new HibernateModule(),
            new JSONModule(),
            new RSAModule(),
            new InventoryServletModule());

    @Override
    protected Injector getInjector() {
        return injector;
    }
}