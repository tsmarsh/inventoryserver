package com.tailoredshapes.inventoryserver.serialisers;

import com.tailoredshapes.inventoryserver.model.Metric;
import org.json.JSONObject;

public class MetricSeriliser implements Serialiser<Metric> {

    @Override
    public byte[] serialise(Metric object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", object.getId());
        jsonObject.put("value", object.getValue());
        jsonObject.put("type", object.getType().getName());
        return jsonObject.toString().getBytes();
    }
}
