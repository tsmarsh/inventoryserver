package com.tailoredshapes.inventoryserver.serialisers;


import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

public interface Serialisers {
    StreamSerializer<Category> categorySerialiser = new StreamSerializer<Category>() {
        @Override
        public void write(ObjectDataOutput out, Category category) throws IOException {
            out.writeLong(category.getId());
            out.writeUTF(category.getName());
            out.writeUTF(category.getFullname());
            if(category.getParent() != null){
                out.writeObject(category.getParent());
            }
        }

        @Override
        public Category read(ObjectDataInput in) throws IOException {
            return new Category()
                    .setId(in.readLong())
                    .setName(in.readUTF())
                    .setFullname(in.readUTF())
                    .setParent(in.readObject());

        }

    }

    StreamSerializer<Metric> metricSerialiser = new StreamSerializer<Metric>() {
        @Override
        public void write(ObjectDataOutput out, Metric metric) throws IOException {
            out.writeLong(metric.getId());
            out.writeUTF(metric.getValue());
            out.writeUTF(metric.getType().getName());
        }

        @Override
        public Metric read(ObjectDataInput in) throws IOException {
            return null;
        }
    }



            (object) -> {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", object.getId());
        jsonObject.put("value", object.getValue());
        jsonObject.put("type", object.getType().getName());
        return jsonObject.toString();
    };

    BiFunction<UrlBuilder<Inventory>, Serialiser<Metric>, StreamSerializer<Inventory>> inventorySerializerBuilder =
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

    BiFunction<UrlBuilder<User>, StreamSerializer<Inventory>, StreamSerializer<User>> userSerializerBuilder =
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

