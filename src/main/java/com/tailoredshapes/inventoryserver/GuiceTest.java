package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class GuiceTest {
    public static Injector injector = Guice.createInjector(
            new InventoryServerModule("localhost", 5555),
            new InMemoryModule(),
            new JSONModule(),
            new RSAModule());

    public static Injector hibernateInjector = Guice.createInjector(
            new InventoryServerModule("localhost", 5555),
            new HibernateModule(),
            new JSONModule(),
            new RSAModule());
}
