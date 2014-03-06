package com.tailoredshapes.inventoryserver.handlers;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InventoryUrlBuilderTest {
    @Test
    public void testShouldReturnTheCorrectUrlForAnInventory() throws Exception {
        User user = new UserBuilder().id(-110584l).build();
        Inventory inventory = new InventoryBuilder()
                .id(141211l)
                .user(user).build();

        InventoryUrlBuilder inventoryUrlBuilder = new InventoryUrlBuilder("http", "test.domain", 80);
        String url = inventoryUrlBuilder.build(inventory);
        assertEquals(String.format("http://test.domain:80/users/%s/inventories/%s", user.getId(), inventory.getId()), url);
    }
}
