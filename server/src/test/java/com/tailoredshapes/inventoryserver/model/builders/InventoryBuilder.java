package com.tailoredshapes.inventoryserver.model.builders;

import java.util.ArrayList;
import java.util.List;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;

public class InventoryBuilder {

  Long id = null;
  Category category;
  List<Metric> metrics;
  Inventory parent = null;

  public InventoryBuilder() {
    category = new CategoryBuilder().build();
    metrics = new ArrayList<>();

  }

  public InventoryBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public InventoryBuilder metrics(List<Metric> metrics) {
    this.metrics = metrics;
    return this;
  }

  public InventoryBuilder parent(Inventory parent) {
    this.parent = parent;
    return this;
  }

  public InventoryBuilder category(Category category) {
    this.category = category;
    return this;
  }

  public Inventory build() {
    return new Inventory()
      .setId(id)
      .setMetrics(metrics)
      .setCategory(category)
      .setParent(parent);
  }
}
