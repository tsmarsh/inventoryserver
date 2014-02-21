package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.net.httpserver.HttpServer;
import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class InventoryModuleTest {
    @Test
    public void shouldCreateAServer() throws Exception {
        Injector injector = Guice.createInjector(new InventoryModule("0.0.0.0", 5555));
        HttpServer server = injector.getInstance(HttpServer.class);
        assertNotNull(server);
    }
}
