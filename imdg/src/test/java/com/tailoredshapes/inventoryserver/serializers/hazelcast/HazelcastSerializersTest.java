package com.tailoredshapes.inventoryserver.serializers.hazelcast;


import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.tailoredshapes.inventoryserver.InMemoryDataGridProviders;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.serialisers.hazelcast.HazelcastSerialisers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static com.tailoredshapes.underbar.UnderBar.first;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HazelcastSerializersTest {

    private static HazelcastInstance instance;

    @BeforeClass
    public static void setupHazelcast() {
        Config cfg = InMemoryDataGridProviders.buildConfig();
        instance = Hazelcast.newHazelcastInstance(cfg);
    }

    @AfterClass
    public static void destroyHazelcast() {
        instance.shutdown();
    }

    MetricType metricType = new MetricType().setId(1414l).setName("metric");
    Metric metric = new Metric().setId(1010l).setType(metricType).setValue("foo");
    Category category = new Category().setId(1617l).setFullname("com").setName("com").setParent(null);
    Category categoryWithParent = new Category().setId(1616l).setFullname("com.foo").setName("foo").setParent(category);

    List<Metric> metricList = new ArrayList();
    Inventory inventory = new Inventory().setId(1205l).setParent(null).setCategory(category).setMetrics(metricList);

    ArrayList<Inventory> inventorySet = new ArrayList<>();

    User user = new User().setCreated(new Date()).setId(0101l).setName("Bob Test").setInventories(inventorySet);

    Inventory invWithParent = new Inventory().setId(1206l).setParent(inventory).setCategory(category).setMetrics(new ArrayList<>());

    @Test
    public void shouldAlwaysReturnAPositiveId() {
        assertTrue(HazelcastSerialisers.inventorySerializer.getTypeId() > 0);
        assertTrue(HazelcastSerialisers.categorySerialiser.getTypeId() > 0);
        assertTrue(HazelcastSerialisers.metricSerialiser.getTypeId() > 0);
        assertTrue(HazelcastSerialisers.metricTypeSerialiser.getTypeId() > 0);
        assertTrue(HazelcastSerialisers.userSerializer.getTypeId() > 0);
    }

    @Test
    public void shouldSerializeAMetricType() {
        IMap<String, MetricType> test = instance.getMap("metricType");
        test.put("test", metricType);
        MetricType test1 = test.get("test");
        assertEquals(test1.getName(), metricType.getName());
    }

    @Test
    public void shouldSerializeAMetric() {
        IMap<String, Metric> test = instance.getMap("metric");
        test.put("test", metric);
        Metric test1 = test.get("test");
        assertEquals(test1.getType().getName(), metricType.getName());
        assertEquals(test1.getValue(), metric.getValue());
    }

    @Test
    public void shouldSerializeACategory() {
        IMap<String, Category> test = instance.getMap("category");
        test.put("test", category);
        Category test1 = test.get("test");
        assertEquals(test1.getFullname(), category.getFullname());
    }

    @Test
    public void shouldSerializeACategoryWithParent() {
        IMap<String, Category> test = instance.getMap("category");
        test.put("test", categoryWithParent);
        Category test1 = test.get("test");
        assertEquals(test1.getParent(), category);
    }

    @Test
    public void shouldSerializeAnInventory() {
        metricList.add(metric);
        IMap<String, Inventory> test = instance.getMap("inventory");
        test.put("test", inventory);
        Inventory test1 = test.get("test");
        assertEquals(test1.getCategory(), inventory.getCategory());
        assertEquals(first(test1.getMetrics()), metric);
    }

    @Test
    public void shouldSerializeAnInventoryWithParent() {
        IMap<String, Inventory> test = instance.getMap("inventory");
        test.put("test", invWithParent);
        Inventory test1 = test.get("test");
        assertEquals(inventory, test1.getParent());
    }

    @Test
    public void shouldSerializeAUser() {
        IMap<String, User> test = instance.getMap("user");
        inventorySet.add(inventory);
        test.put("test", user);
        User test1 = test.get("test");

        assertTrue(test1.getInventories().contains(inventory));
    }
}
