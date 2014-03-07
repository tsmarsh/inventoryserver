package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;
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
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class InventorySerialiserTest {

    private Inventory parent;
    private User user;
    private Category build;
    private UrlBuilder<Inventory> inventoryUrlBuilder;
    private UrlBuilder<User> userUrlBuilder;
    private Serialiser<Metric> metricSerialiser;

    @Before
    public void setUp() throws Exception {
        user = new UserBuilder().id(49295122l).build();
        parent = new InventoryBuilder().id(-111111111111l).user(user).build();
        build = new CategoryBuilder().fullname("archer.face").build();
        inventoryUrlBuilder = new InventoryUrlBuilder("http", "tailoredshapes.com", 3333);
        userUrlBuilder = new UserUrlBuilder("https", "tailoredshapes.com", 80);
        metricSerialiser = new JSONSerialiser<>();
    }


    @Test
    public void testSerialiseACompeteInventory() throws Exception {
        Inventory inventory = new InventoryBuilder().id(777777777777777l)
                .user(user)
                .category(build)
                .parent(parent).build();

        InventorySerialiser inventorySerialiser = new InventorySerialiser(inventoryUrlBuilder, userUrlBuilder, metricSerialiser);
        JSONObject jsonObject = new JSONObject(new String(inventorySerialiser.serialise(inventory)));
        assertEquals(inventory.getId().longValue(), jsonObject.getLong("id"));
        assertEquals(build.getFullname(), jsonObject.getString("category"));
        assertEquals(inventoryUrlBuilder.build(parent), jsonObject.getString("parent"));
        assertEquals(userUrlBuilder.build(user), jsonObject.getString("user"));
        assertEquals(0, jsonObject.getJSONArray("metrics").length());
    }

    @Test
    public void testSerialiseAnOrphanedInventory() throws Exception {
        Inventory inventory = new InventoryBuilder().id(777777777777777l)
                .user(user)
                .category(build).build();

        InventorySerialiser inventorySerialiser = new InventorySerialiser(inventoryUrlBuilder, userUrlBuilder, metricSerialiser);
        JSONObject jsonObject = new JSONObject(new String(inventorySerialiser.serialise(inventory)));
        assertEquals(inventory.getId().longValue(), jsonObject.getLong("id"));
        assertEquals(build.getFullname(), jsonObject.getString("category"));
        assertFalse(jsonObject.has("parent"));
        assertEquals(userUrlBuilder.build(user), jsonObject.getString("user"));
        assertEquals(0, jsonObject.getJSONArray("metrics").length());
    }

    @Test
    public void testSerialiseAnInventoryWithMetrics() throws Exception {
        Inventory inventory = new InventoryBuilder().id(777777777777777l)
                .user(user)
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
        assertEquals(userUrlBuilder.build(user), jsonObject.getString("user"));
        assertEquals(2, jsonObject.getJSONArray("metrics").length());
        assertEquals(1l, jsonObject.getJSONArray("metrics").getJSONObject(0).getLong("id"));
        assertEquals(2l, jsonObject.getJSONArray("metrics").getJSONObject(1).getLong("id"));
    }
}
