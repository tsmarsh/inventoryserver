package com.tailoredshapes.inventoryserver.parsers;

import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InventoryParser<A, B> implements Parser<Inventory> {

    private final Repository<Category, A> categoryRepository;
    private final Repository<Inventory, ?> inventoryRepository;
    private final Repository<MetricType, B> metricTypeRepository;
    private final IdExtractor<Long, Inventory> inventoryIdExtractor;
    private final FinderFactory<Category, String, A> finderFactory;
    private final FinderFactory<MetricType, String, B> nameFinderFactory;

    @Inject
    public InventoryParser(Repository<Category, A> categoryRepository,
                           Repository<Inventory, ?> inventoryRepository,
                           Repository<MetricType, B> metricTypeRepository,
                           IdExtractor<Long, Inventory> inventoryIdExtractor,
                           FinderFactory<Category, String, A> finderFactory,
                           FinderFactory<MetricType, String, B> nameFinderFactory) {

        this.categoryRepository = categoryRepository;
        this.inventoryRepository = inventoryRepository;
        this.metricTypeRepository = metricTypeRepository;
        this.inventoryIdExtractor = inventoryIdExtractor;
        this.finderFactory = finderFactory;
        this.nameFinderFactory = nameFinderFactory;
    }

    @Override
    public Inventory parse(String s) {
        Inventory inventory = new Inventory();
        JSONObject jo = new JSONObject(s);

        String categoryFullName = jo.getString("category");

        inventory.setCategory(categoryRepository.findBy(finderFactory.lookFor(categoryFullName)));

        if (jo.has("id")) {
            try {
                inventory.setId(inventoryIdExtractor.extract(new URL(jo.getString("id")).getPath()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }


        if (jo.has("parent")) {
            long parent_id;
            try {
                parent_id = inventoryIdExtractor.extract(new URL(jo.getString("parent")).getPath());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            inventory.setParent(inventoryRepository.findById(parent_id));
        }

        if (jo.has("metrics")) {
            JSONArray jsonMetrics = jo.getJSONArray("metrics");
            inventory.setMetrics(parseMetrics(jsonMetrics));
        }

        return inventory;
    }

    List<Metric> parseMetrics(JSONArray jsonMetrics) {

        List<Metric> metrics = new ArrayList<>(jsonMetrics.length());
        for (int i = 0; i < jsonMetrics.length(); i++) {
            JSONObject jsonObject = jsonMetrics.getJSONObject(i);
            metrics.add(new Metric()
                    .setValue(jsonObject.getString("value"))
                    .setType(metricTypeRepository.findBy(nameFinderFactory.lookFor(jsonObject.getString("type")))));

        }
        return metrics;
    }
}
