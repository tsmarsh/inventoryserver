package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class InventoryMemoryServletConfig extends GuiceServletContextListener {

    private static final Injector injector = Guice.createInjector(
            new UserRootServletModule(false, false),
            new InventoryServerModule("localhost", 6666),
            new InMemoryModule(),
            new JSONModule(),
            new EncoderModule());

    @Override
    protected Injector getInjector() {
        return injector;
    }
}