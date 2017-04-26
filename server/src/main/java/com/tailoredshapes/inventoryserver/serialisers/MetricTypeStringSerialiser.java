package com.tailoredshapes.inventoryserver.serialisers;

import com.tailoredshapes.inventoryserver.model.MetricType;
import org.json.JSONObject;

public class MetricTypeStringSerialiser implements Serialiser<MetricType> {
    @Override
    public String serialise(MetricType object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", object.getId());
        jsonObject.put("name", object.getName());
        return jsonObject.toString();
    }
}
