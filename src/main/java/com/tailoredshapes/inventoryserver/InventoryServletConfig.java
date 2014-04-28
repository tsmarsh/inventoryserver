package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

public class InventoryServletConfig extends GuiceServletContextListener {

    private static final Injector injector = Guice.createInjector(
            new JpaPersistModule("inventory_server"),
            new UserRootServletModule(true, true),
            new InventoryServerModule("localhost", 7777),
            new HibernateModule(),
            new JSONModule(),
            new EncoderModule());

    @Override
    protected Injector getInjector() {
        return injector;
    }
}