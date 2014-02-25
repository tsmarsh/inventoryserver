package com.tailoredshapes.inventoryserver.utils;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class InventoryParser implements Parser<Inventory> {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryRepository inventoryRepository;
    private final MetricTypeRepository metricTypeRepository;

    @Inject
    public InventoryParser(UserRepository userRepository, CategoryRepository categoryRepository, InventoryRepository inventoryRepository, MetricTypeRepository metricTypeRepository) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
        this.metricTypeRepository = metricTypeRepository;
    }

    @Override
    public Inventory parse(String s) {
        Inventory inventory = new Inventory();
        JSONObject jo = new JSONObject(s);

        long user_id = jo.getLong("user_id");
        inventory.setUser(userRepository.findById(user_id));

        String categoryFullName = jo.getString("category");
        inventory.setCategory(categoryRepository.findByFullname(inventory.getUser(), categoryFullName));

        if (jo.has("id")) {
            inventory.setId(jo.getLong("id"));
        }


        if (jo.has("parent_id")) {
            long parent_id = jo.getLong("parent_id");
            inventory.setParent(inventoryRepository.findById(inventory.getUser(), parent_id));
        }

        if (jo.has("metrics")) {
            JSONArray jsonMetrics = jo.getJSONArray("metrics");
            inventory.setMetrics(parseMetrics(inventory.getUser(), jsonMetrics));
        }

        return inventory;
    }

    List<Metric> parseMetrics(User user, JSONArray jsonMetrics) {

        List<Metric> metrics = new ArrayList<>(jsonMetrics.length());
        for (int i = 0; i < jsonMetrics.length(); i++) {
            JSONObject jsonObject = jsonMetrics.getJSONObject(i);
            metrics.add(new Metric()
                    .setValue(jsonObject.getString("value"))
                    .setType(metricTypeRepository.findByName(user, jsonObject.getString("type"))));

        }
        return metrics;
    }
}
