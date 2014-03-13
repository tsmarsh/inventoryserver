package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import org.junit.Before;
import org.junit.Test;

import static com.tailoredshapes.inventoryserver.GuiceTest.injector;
import static org.junit.Assert.assertEquals;

public class InMemoryInventoryRepositoryTest {

    private DAO<Inventory> inventoryDAO;
    private InMemoryInventoryRepository repo;

    @Before
    public void setUp() throws Exception {
        repo = injector.getInstance(new Key<InMemoryInventoryRepository>() {});
        inventoryDAO = injector.getInstance(new Key<DAO<Inventory>>() {});
    }

    @Test
    public void testFindById() throws Exception {
        User user = new UserBuilder().id(null).build();
        Inventory inventory = new InventoryBuilder().user(user).build();
        Inventory savedInventory = inventoryDAO.create(inventory);
        Inventory byId = repo.findById(savedInventory.getId());
        assertEquals(savedInventory, byId);
    }
}
