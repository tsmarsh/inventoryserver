package com.tailoredshapes.inventoryserver.serialisers;

import com.tailoredshapes.inventoryserver.model.Metric;
import org.json.JSONObject;

public class MetricStringSerialiser implements Serialiser<Metric, String> {

    @Override
    public String serialise(Metric object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", object.getId());
        jsonObject.put("value", object.getValue());
        jsonObject.put("type", object.getType().getName());
        return jsonObject.toString();
    }
}
