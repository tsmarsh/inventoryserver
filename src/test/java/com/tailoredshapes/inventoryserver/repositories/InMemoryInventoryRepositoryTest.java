package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InMemoryInventoryRepositoryTest {


    private Serialiser<Inventory> serialiser;
    private Encoder encoder;
    private InMemoryDAO<Inventory> inventoryDAO;

    @Before
    public void setUp() throws Exception {
        serialiser = new JSONSerialiser<>();
        encoder = new RSAEncoder();
        inventoryDAO = new InMemoryDAO<>(serialiser, encoder);
    }

    @Test
    public void testFindById() throws Exception {
        User user = new UserBuilder().build();
        Inventory inventory = new InventoryBuilder().user(user).build();
        InMemoryInventoryRepository repo = new InMemoryInventoryRepository(inventoryDAO);
        Inventory savedInventory = inventoryDAO.create(user, inventory);
        Inventory byId = repo.findById(user, savedInventory.getId());
        assertEquals(savedInventory, byId);
    }
}
