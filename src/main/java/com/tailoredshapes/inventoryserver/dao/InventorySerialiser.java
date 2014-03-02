package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.handlers.UrlBuilder;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

public class InventorySerialiser implements Serialiser<Inventory> {

    private final UrlBuilder<Inventory> inventoryUrlBuilder;
    private final UrlBuilder<User> userUrlBuilder;
    private Serialiser<Metric> metricSerializer;

    @Inject
    public InventorySerialiser(UrlBuilder<Inventory> inventoryUrlBuilder, UrlBuilder<User> userUrlBuilder, Serialiser<Metric> metricSerializer) {

        this.inventoryUrlBuilder = inventoryUrlBuilder;
        this.userUrlBuilder = userUrlBuilder;
        this.metricSerializer = metricSerializer;
    }

    @Override
    public byte[] serialise(Inventory object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", object.getId());
        jsonObject.put("category", object.getCategory().getFullname() );
        jsonObject.put("user", userUrlBuilder.build(object.getUser()));
        JSONArray metrics = new JSONArray();
        for(Metric metric : object.getMetrics()){
            metrics.put(new JSONObject(new String(metricSerializer.serialise(metric))));
        }

        jsonObject.put("metrics", metrics);

        if(null != object.getParent()){
            jsonObject.put("parent", inventoryUrlBuilder.build(object.getParent()));
        }
        return jsonObject.toString().getBytes();
    }
}
