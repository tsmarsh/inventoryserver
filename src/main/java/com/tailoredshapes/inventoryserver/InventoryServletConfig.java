package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;

public class InventoryServletConfig {

    public static final Injector injector = Guice.createInjector(
            new JpaPersistModule("inventory_server"));
}

