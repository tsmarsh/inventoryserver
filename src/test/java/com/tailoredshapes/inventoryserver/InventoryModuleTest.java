package com.tailoredshapes.inventoryserver;

import com.google.inject.Key;
import com.sun.net.httpserver.HttpServer;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.security.SHA;
import com.tailoredshapes.inventoryserver.servlets.Pestlet;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InventoryModuleTest {

    @Test
    public void testShouldReturnTheSameDAORegardlessOfInterface() throws Exception {
        DAO<Category> dao1 = GuiceTest.injector.getInstance(new Key<DAO<Category>>() {});
        DAO<Category> dao2 = GuiceTest.injector.getInstance(new Key<InMemoryDAO<Category, SHA>>() {});

        assertEquals(dao1, dao2);
    }
}
