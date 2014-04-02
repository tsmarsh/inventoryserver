package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

public class GuiceTest {
    public static Injector hibernateInjector;

    static {
        hibernateInjector = Guice.createInjector(
                new JpaPersistModule("inventory_server"),
                new InventoryServerModule("localhost", 5555),
                new HibernateModule(),
                new JSONModule(),
                new RSAModule());

        PersistService instance = hibernateInjector.getInstance(PersistService.class);
        instance.start();

    }

    public GuiceTest() {



    }

    public static Injector injector = Guice.createInjector(
            new InventoryServerModule("localhost", 5555),
            new InMemoryModule(),
            new JSONModule(),
            new RSAModule());


}
