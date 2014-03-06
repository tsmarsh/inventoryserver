package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import com.tailoredshapes.inventoryserver.serialisers.MetricSeriliser;
import junit.framework.TestCase;
import org.json.JSONObject;

public class MetricSeriliserTest extends TestCase {
    public void testSerialise() throws Exception {
        Metric metric = new MetricBuilder().id(555l).build();
        MetricSeriliser metricSeriliser = new MetricSeriliser();
        JSONObject jsonObject = new JSONObject(new String(metricSeriliser.serialise(metric)));
        assertEquals(metric.getId().longValue(), jsonObject.getLong("id"));
        assertEquals(metric.getValue(), jsonObject.getString("value"));
        assertEquals(metric.getType().getName(), jsonObject.getString("type"));
    }
}
