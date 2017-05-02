package com.tailoredshapes.inventoryserver.dao;

import java.util.ArrayList;
import java.util.List;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;

public class InventorySaver implements Saver<Inventory> {

  private final DAO<Metric> metricDAO;
  private final DAO<Category> categoryDAO;

  public InventorySaver(
    DAO<Metric> metricDAO,
    DAO<Category> categoryDAO) {
    this.metricDAO = metricDAO;
    this.categoryDAO = categoryDAO;
  }

  @Override
  public Inventory saveChildren(DAO<Inventory> inventoryDAO, Inventory object) {
    object.setParent(inventoryDAO.upsert(object.getParent()));
    object.setCategory(categoryDAO.upsert(object.getCategory()));
    List<Metric> savedMetrics = new ArrayList<>();

    for (Metric metric : object.getMetrics()) {
      savedMetrics.add(metricDAO.upsert(metric));
    }

    object.setMetrics(savedMetrics);
    return object;
  }
}

