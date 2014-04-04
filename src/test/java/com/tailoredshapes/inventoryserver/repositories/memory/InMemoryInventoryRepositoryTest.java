package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.tailoredshapes.inventoryserver.GuiceTest.injector;
import static com.tailoredshapes.inventoryserver.HibernateTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class InMemoryInventoryRepositoryTest {

    private DAO<Inventory> inventoryDAO;
    private InMemoryInventoryRepository repo;

    private SimpleScope scope;

    @Before
    public void setUp() throws Exception {
        scope = injector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(User.class, new User());

        inventoryDAO = injector.getInstance(new Key<DAO<Inventory>>() {});
        repo = injector.getInstance(new Key<InMemoryInventoryRepository>() {});
    }

    @After
    public void tearDown() throws Exception {
        scope.exit();
    }
    @Test
    public void testFindById() throws Exception {
        Inventory inventory = new InventoryBuilder().build();
        Inventory savedInventory = inventoryDAO.create(inventory);
        Inventory byId = repo.findById(savedInventory.getId());
        assertEquals(savedInventory, byId);
    }
}
