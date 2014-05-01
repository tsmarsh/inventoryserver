package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;

public class InventoryServletConfig {

    protected static final Injector injector = Guice.createInjector(
            new JpaPersistModule("inventory_server"));
}

