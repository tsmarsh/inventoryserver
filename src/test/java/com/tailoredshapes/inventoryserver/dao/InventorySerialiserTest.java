package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.serialisers.InventorySerialiser;
import com.tailoredshapes.inventoryserver.serialisers.JSONSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class InventorySerialiserTest {

    private Inventory parent;
    private Category build;
    private UrlBuilder<Inventory> inventoryUrlBuilder;
    private UrlBuilder<User> userUrlBuilder;
    private Serialiser<Metric> metricSerialiser;

    @Before
    public void setUp() throws Exception {
        parent = new InventoryBuilder().id(-111111111111l).build();
        build = new CategoryBuilder().fullname("archer.face").build();
        inventoryUrlBuilder = new InventoryUrlBuilder("http", "tailoredshapes.com", 3333);
        userUrlBuilder = new UserUrlBuilder("https", "tailoredshapes.com", 80);
        metricSerialiser = new JSONSerialiser<>();
    }


    @Test
    public void testSerialiseACompeteInventory() throws Exception {
        Inventory inventory = new InventoryBuilder().id(777777777777777l)
                .category(build)
                .parent(parent).build();

        InventorySerialiser inventorySerialiser = new InventorySerialiser(inventoryUrlBuilder, userUrlBuilder, metricSerialiser);
        JSONObject jsonObject = new JSONObject(new String(inventorySerialiser.serialise(inventory)));
        assertEquals(inventory.getId().longValue(), jsonObject.getLong("id"));
        assertEquals(build.getFullname(), jsonObject.getString("category"));
        assertEquals(inventoryUrlBuilder.build(parent), jsonObject.getString("parent"));
        assertEquals(0, jsonObject.getJSONArray("metrics").length());
    }

    @Test
    public void testSerialiseAnOrphanedInventory() throws Exception {
        Inventory inventory = new InventoryBuilder().id(777777777777777l)
                .category(build).build();

        InventorySerialiser inventorySerialiser = new InventorySerialiser(inventoryUrlBuilder, userUrlBuilder, metricSerialiser);
        JSONObject jsonObject = new JSONObject(new String(inventorySerialiser.serialise(inventory)));
        assertEquals(inventory.getId().longValue(), jsonObject.getLong("id"));
        assertEquals(build.getFullname(), jsonObject.getString("category"));
        assertFalse(jsonObject.has("parent"));
        assertEquals(0, jsonObject.getJSONArray("metrics").length());
    }

    @Test
    public void testSerialiseAnInventoryWithMetrics() throws Exception {
        Inventory inventory = new InventoryBuilder().id(777777777777777l)
                .category(build)
                .metrics(new ArrayList<Metric>() {{
                    add(new MetricBuilder().id(1l).build());
                    add(new MetricBuilder().id(2l).build());
                }})
                .parent(parent).build();

        InventorySerialiser inventorySerialiser = new InventorySerialiser(inventoryUrlBuilder, userUrlBuilder, metricSerialiser);
        JSONObject jsonObject = new JSONObject(new String(inventorySerialiser.serialise(inventory)));
        assertEquals(inventory.getId().longValue(), jsonObject.getLong("id"));
        assertEquals(build.getFullname(), jsonObject.getString("category"));
        assertEquals(inventoryUrlBuilder.build(parent), jsonObject.getString("parent"));
        assertEquals(2, jsonObject.getJSONArray("metrics").length());
        assertEquals(1l, jsonObject.getJSONArray("metrics").getJSONObject(0).getLong("id"));
        assertEquals(2l, jsonObject.getJSONArray("metrics").getJSONObject(1).getLong("id"));
    }
}
