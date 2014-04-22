package com.tailoredshapes.inventoryserver.serialisers;

import com.google.inject.servlet.RequestScoped;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

public class InventorySerialiser implements Serialiser<Inventory, byte[]> {

    private final Serialiser<Metric, String> metricSerializer;

    @Inject
    public InventorySerialiser(Serialiser<Metric, String> metricSerializer) {
        this.metricSerializer = metricSerializer;
    }

    @Override
    public byte[] serialise(Inventory object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("category", object.getCategory().getFullname());
        JSONArray metrics = new JSONArray();
        for (Metric metric : object.getMetrics()) {
            metrics.put(new JSONObject(metricSerializer.serialise(metric)));
        }

        jsonObject.put("metrics", metrics);

        if (null != object.getParent()) {
            jsonObject.put("parent", object.getParent().getId());
        }
        return jsonObject.toString().getBytes();
    }
}

