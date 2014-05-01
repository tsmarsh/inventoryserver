package com.tailoredshapes.inventoryserver;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class UserRootInMemoryServletConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return InventoryMemoryServletConfig.injector.createChildInjector(
                new InventoryServerModule("localhost", 6666),
                new InMemoryModule(),
                new JSONModule(),
                new EncoderModule(),
                new UserRootServletModule(false, false),
                new UserRootInMemoryRepositoryModule()
        );
    }
}
