package com.tailoredshapes.inventoryserver.serialisers;


import java.util.function.BiFunction;
import java.util.function.Function;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Serialisers {
  Serialiser<Category> categorySerialiser = (object) -> {
    Function<Category, JSONObject> mostOfIt = (cat) -> {
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("id", object.getId());
      jsonObject.put("name", object.getName());
      jsonObject.put("fullname", object.getFullname());
      return jsonObject;
    };

    JSONObject js = mostOfIt.apply(object);
    if (object.getParent() != null) {
      js.put("parent", mostOfIt.apply(object.getParent()));
    }
    return js.toString();
  };

  Serialiser<Metric> metricSerialiser = (object) -> {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("id", object.getId());
    jsonObject.put("value", object.getValue());
    jsonObject.put("type", object.getType().getName());
    return jsonObject.toString();
  };

  BiFunction<UrlBuilder<Inventory>, Serialiser<Metric>, Serialiser<Inventory>> inventorySerializerBuilder =
    (urlBuilder, serialiser) ->
      (object) -> {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", urlBuilder.build(object));
        jsonObject.put("category", object.getCategory().getFullname());
        JSONArray metrics = new JSONArray();
        for (Metric metric : object.getMetrics()) {
          metrics.put(new JSONObject(serialiser.serialise(metric)));
        }

        jsonObject.put("metrics", metrics);

        if (null != object.getParent()) {
          jsonObject.put("parent", urlBuilder.build(object.getParent()));
        }
        return jsonObject.toString();
      };

  BiFunction<UrlBuilder<User>, Serialiser<Inventory>, Serialiser<User>> userSerializerBuilder =
    (urlBuilder, inventorySerialiser) ->
      (user) -> {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", urlBuilder.build(user));
        jsonObject.put("name", user.getName());

        JSONArray inventories = new JSONArray();
        user.getInventories().forEach((inventory -> inventories.put(new JSONObject(inventorySerialiser.serialise(inventory)))));

        jsonObject.put("inventories", inventories);

        return jsonObject.toString();
      };
}
