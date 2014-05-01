package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class InventoryMemoryServletConfig{

    protected static final Injector injector = Guice.createInjector(
        new InMemoryDBModule()
    );
}

