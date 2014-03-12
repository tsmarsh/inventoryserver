package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.sun.net.httpserver.HttpServer;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.security.SHA;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InventoryModuleTest {

    @Test
    public void shouldCreateAServer() throws Exception {
        HttpServer server = GuiceTest.injector.getInstance(HttpServer.class);
        assertNotNull(server);
    }

    @Test
    public void testShouldReturnTheSameDAORegardlessOfInterface() throws Exception {
        DAO<Category> dao1 = GuiceTest.injector.getInstance(new Key<DAO<Category>>() {});
        DAO<Category> dao2 = GuiceTest.injector.getInstance(new Key<InMemoryDAO<Category, SHA>>() {});

        assertEquals(dao1, dao2);
    }
}
