package com.tailoredshapes.inventoryserver.model;

import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

public class MetricTest {
    @Test
    public void testEquals() throws Exception {
        Metric a = new MetricBuilder().id(3141592654l).value("Cassie").build();
        Metric b = a.clone();
        Metric c = b.clone().setValue("Archer");

        assertTrue(a.equals(b));
        assertFalse(a.equals(c));

    }

    @Test
    public void testHashCode() throws Exception {
        Metric a = new MetricBuilder().id(3141592654l).value("Cassie").build();
        Metric b = a.clone();
        Metric c = b.clone().setValue("Archer");

        assertEquals(a.hashCode(), b.hashCode());

        assertThat(a.hashCode(), not(c.hashCode()));
    }
}
