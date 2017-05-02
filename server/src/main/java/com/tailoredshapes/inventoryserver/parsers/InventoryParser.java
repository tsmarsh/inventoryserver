package com.tailoredshapes.inventoryserver.parsers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.Looker;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import org.json.JSONArray;
import org.json.JSONObject;

public interface InventoryParser {

  static <A, B> Parser<Inventory> inventoryParser(Repository.FindBy<Category, A> categoryRepository,
                                                  Repository.FindById<Inventory> inventoryRepository,
                                                  Repository.FindBy<MetricType, B> metricTypeRepository,
                                                  Looker<String, Category, A> finderFactory,
                                                  Looker<String, MetricType, B> nameFinderFactory,
                                                  IdExtractor<Long> inventoryIdExtractor) {

    return (String s) -> {
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
        inventory.setMetrics(parseMetrics(metricTypeRepository,
                                          nameFinderFactory,
                                          jsonMetrics));
      }

      return inventory;
    };

  }


  static <B> List<Metric> parseMetrics(Repository.FindBy<MetricType, B> metricTypeRepository,
                                       Looker<String, MetricType, B> nameFinderFactory,
                                       JSONArray jsonMetrics) {

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
