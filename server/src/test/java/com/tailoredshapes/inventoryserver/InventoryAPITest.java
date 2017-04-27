package com.tailoredshapes.inventoryserver;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Inventory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class InventoryAPITest {

    @BeforeClass
    public static void start(){
        Server.start();
    }

    @Test
    public void shouldReturnAllInventories() throws Exception {
        DefaultApi api = new DefaultApi();
        List<Inventory> inventories = api.allInventories();
        assertEquals(0, inventories.size());
    }
}
