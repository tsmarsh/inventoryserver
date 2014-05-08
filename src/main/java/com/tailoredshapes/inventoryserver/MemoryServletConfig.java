package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.tailoredshapes.inventoryserver.modules.InventoryServerModule;
import com.tailoredshapes.inventoryserver.modules.RoutesModule;
import com.tailoredshapes.inventoryserver.modules.memory.InMemoryDBModule;
import com.tailoredshapes.inventoryserver.modules.memory.InventoryRootMemoryModule;
import com.tailoredshapes.inventoryserver.modules.memory.UserRootMemoryModule;

public class MemoryServletConfig extends GuiceServletContextListener{

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new InMemoryDBModule(),
                new InventoryServerModule("localhost", 6666),
                new RoutesModule(false, false),
                new UserRootMemoryModule(),
                new InventoryRootMemoryModule());
    }
}

