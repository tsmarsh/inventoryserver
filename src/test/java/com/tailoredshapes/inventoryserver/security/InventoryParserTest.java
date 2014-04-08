package com.tailoredshapes.inventoryserver.security;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricTypeBuilder;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class InventoryParserTest {

    private InventoryParser parser;
    private Serialiser<Inventory, String> serialiser;
    private SimpleScope scope;


    @Before
    public void init() {
        scope = GuiceTest.injector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), new User().setId(141211l));
        parser = GuiceTest.injector.getInstance(InventoryParser.class);
        serialiser = GuiceTest.injector.getInstance(new Key<Serialiser<Inventory, String>>() {});
    }

    @After
    public void tearDown() throws Exception {
        scope.exit();
    }

    @Test
    public void shouldParseASimpleInventory() throws Exception {
        Inventory inventory = new InventoryBuilder().build();

        Inventory inv = parser.parse(serialiser.serialise(inventory));
        assertEquals(inventory.getCategory().getFullname(), inv.getCategory().getFullname());
    }

    @Test
    public void shouldParseAnInventoryWithParent() throws Exception {


        parser = GuiceTest.injector.getInstance(InventoryParser.class);
        serialiser = GuiceTest.injector.getInstance(new Key<Serialiser<Inventory, String>>() {});
        DAO<Inventory> dao = GuiceTest.injector.getInstance(new Key<DAO<Inventory>>() {});

        Inventory parent = new InventoryBuilder().build();
        parent = dao.create(parent);

        Inventory inventory = new InventoryBuilder().parent(parent).build();

        Inventory inv = parser.parse(serialiser.serialise(inventory));

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

        Inventory inventory = new InventoryBuilder().metrics(metrics).build();


        Inventory inv = parser.parse(new String(serialiser.serialise(inventory)));

        assertEquals(inv.getMetrics().get(0).getValue(), "Cassie");
        assertEquals(inv.getMetrics().get(0).getType().getName(), testType.getName());
        assertEquals(inv.getMetrics().get(1).getValue(), "Archer");
        assertEquals(inv.getMetrics().get(1).getType().getName(), testType.getName());
    }
}
