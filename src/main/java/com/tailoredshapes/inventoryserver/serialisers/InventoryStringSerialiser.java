package com.tailoredshapes.inventoryserver.serialisers;

import javax.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class InventoryStringSerialiser implements Serialiser<Inventory, String> {

    private final UrlBuilder<Inventory> inventoryUrlBuilder;
    private final Serialiser<Metric, String> metricSerializer;

    @Inject
    public InventoryStringSerialiser(UrlBuilder<Inventory> inventoryUrlBuilder, Serialiser<Metric, String> metricSerializer) {

        this.inventoryUrlBuilder = inventoryUrlBuilder;
        this.metricSerializer = metricSerializer;
    }

    @Override
    public String serialise(Inventory object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", inventoryUrlBuilder.build(object));
        jsonObject.put("category", object.getCategory().getFullname());
        JSONArray metrics = new JSONArray();
        for (Metric metric : object.getMetrics()) {
            metrics.put(new JSONObject(metricSerializer.serialise(metric)));
        }

        jsonObject.put("metrics", metrics);

        if (null != object.getParent()) {
            jsonObject.put("parent", inventoryUrlBuilder.build(object.getParent()));
        }
        return jsonObject.toString();
    }
}
