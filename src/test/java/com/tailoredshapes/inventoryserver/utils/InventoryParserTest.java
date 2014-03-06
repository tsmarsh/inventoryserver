package com.tailoredshapes.inventoryserver.utils;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.InventoryModule;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricTypeBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class InventoryParserTest {

    private Injector injector = Guice.createInjector(new InventoryModule("localhost", 6666));
    private InventoryParser parser;
    private User testUser;
    private Serialiser<Inventory> serialiser;


    @Before
    public void init() {
        parser = injector.getInstance(InventoryParser.class);
        DAO<User> userDAO = injector.getInstance(new Key<DAO<User>>() {});
        testUser = userDAO.create(new UserBuilder().id(null).build());
        serialiser = injector.getInstance(new Key<Serialiser<Inventory>>() {});
    }

    @Test
    public void shouldParseASimpleInventory() throws Exception {
        Inventory inventory = new InventoryBuilder().user(testUser).build();

        Inventory inv = parser.parse(new String(serialiser.serialise(inventory)));
        assertEquals(inventory.getCategory().getFullname(), inv.getCategory().getFullname());
        assertEquals(testUser, inv.getUser());
    }

    @Test
    public void shouldParseAnInventoryWithParent() throws Exception {
        Inventory parent = new InventoryBuilder().user(testUser).build();
        DAO<Inventory> dao = injector.getInstance(new Key<DAO<Inventory>>() {});
        parent = dao.create(parent);

        Inventory inventory = new InventoryBuilder().user(testUser).parent(parent).build();

        Inventory inv = parser.parse(new String(serialiser.serialise(inventory)));

        assertEquals(parent, inv.getParent());
    }

    @Test
    public void shouldParseAnInventoryWithMetrics() throws Exception {
        List<Metric> metrics = new ArrayList<>(2);
        MetricType testType = new MetricTypeBuilder().build();
        Metric metric1 = new MetricBuilder().type(testType).value("Cassie").build();
        Metric metric2 = new MetricBuilder().type(testType).value("Archer").build();
        metrics.add(metric1);
        metrics.add(metric2);

        Inventory inventory = new InventoryBuilder().user(testUser).metrics(metrics).build();


        Inventory inv = parser.parse(new String(serialiser.serialise(inventory)));

        assertEquals(inv.getMetrics().get(0).getValue(), "Cassie");
        assertEquals(inv.getMetrics().get(0).getType().getName(), testType.getName());
        assertEquals(inv.getMetrics().get(1).getValue(), "Archer");
        assertEquals(inv.getMetrics().get(1).getType().getName(), testType.getName());
    }
}
