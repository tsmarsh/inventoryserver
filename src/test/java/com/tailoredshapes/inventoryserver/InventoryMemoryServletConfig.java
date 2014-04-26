package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.tailoredshapes.inventoryserver.*;

public class InventoryMemoryServletConfig extends GuiceServletContextListener {

    private static final Injector injector = Guice.createInjector(
            new InventoryServletModule(false, false),
            new InventoryServerModule("localhost", 6666),
            new InMemoryModule(),
            new JSONModule(),
            new RSAModule());

    @Override
    protected Injector getInjector() {
        return injector;
    }
}