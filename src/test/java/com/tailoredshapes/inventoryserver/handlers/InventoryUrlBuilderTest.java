package com.tailoredshapes.inventoryserver.handlers;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InventoryUrlBuilderTest {
    @Test
    public void testShouldReturnTheCorrectUrlForAnInventory() throws Exception {
        Inventory inventory = new InventoryBuilder()
                .id(141211l).build();

        InventoryUrlBuilder inventoryUrlBuilder = new InventoryUrlBuilder("http", "test.domain", 80);
        String url = inventoryUrlBuilder.build(inventory);
        assertEquals(String.format("http://test.domain:80/inventories/%s", inventory.getId()), url);
    }
}
