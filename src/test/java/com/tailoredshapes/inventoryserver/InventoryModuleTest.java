package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.sun.net.httpserver.HttpServer;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.utils.SHA;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InventoryModuleTest {

    private Injector injector;

    @Before
    public void setUp() throws Exception {
        injector = Guice.createInjector(new InventoryModule("0.0.0.0", 5555));
    }

    @Test
    public void shouldCreateAServer() throws Exception {
        HttpServer server = injector.getInstance(HttpServer.class);
        assertNotNull(server);
    }

    @Test
    public void testShouldReturnTheSameDAORegardlessOfInterface() throws Exception {
        DAO<Category> dao1 = injector.getInstance(new Key<DAO<Category>>(){});
        DAO<Category> dao2 = injector.getInstance(new Key<InMemoryDAO<Category, SHA>>(){});

        assertEquals(dao1, dao2);
    }
}
