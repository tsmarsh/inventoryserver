package com.tailoredshapes.inventoryserver.model;

import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InventoryTest {
    @Test
    public void testEquals() throws Exception {
        Category category = new CategoryBuilder().build();
        Inventory parent = new InventoryBuilder().build();
        Metric metric = new MetricBuilder().build();
        ArrayList<Metric> metrics = new ArrayList<>();
        metrics.add(metric);

        Inventory a = new InventoryBuilder().category(category).id(3141592654l).metrics(metrics).parent(parent).build();
        Inventory b = a.clone();
        Inventory c = b.clone().setId(666l);
        Inventory d = b.clone().setCategory(new CategoryBuilder().fullname("Archer").build());
        Inventory e = b.clone().setMetrics(Collections.<Metric>emptyList());

        assertTrue(a.equals(b));
        assertFalse(a.equals(c));
        assertFalse(a.equals(d));
        assertFalse(a.equals(e));

    }

    @Test
    public void testHashCode() throws Exception {
        Category category = new CategoryBuilder().build();
        Inventory parent = new InventoryBuilder().build();
        Metric metric = new MetricBuilder().build();
        ArrayList<Metric> metrics = new ArrayList<>();
        metrics.add(metric);

        Inventory a = new InventoryBuilder().category(category).id(3141592654l).metrics(metrics).parent(parent).build();
        Inventory b = a.clone();
        Inventory c = b.clone().setId(666l);
        Inventory d = b.clone().setCategory(new CategoryBuilder().fullname("Archer").build());
        Inventory e = b.clone().setMetrics(Collections.<Metric>emptyList());

        assertTrue(a.hashCode() == b.hashCode());
        assertFalse(a.hashCode() == c.hashCode());
        assertFalse(a.hashCode() == d.hashCode());
        assertFalse(a.hashCode() == e.hashCode());
    }
}
