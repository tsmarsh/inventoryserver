package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.User;

public class InventorySaver extends Saver<Inventory> {

    private final DAO<Inventory> inventoryDAO;
    private final DAO<Metric> metricDAO;
    private final DAO<Category> categoryDAO;

    @Inject
    public InventorySaver(DAO<Inventory> inventoryDAO,
                          DAO<Metric> metricDAO,
                          DAO<Category> categoryDAO) {
        this.inventoryDAO = inventoryDAO;
        this.metricDAO = metricDAO;
        this.categoryDAO = categoryDAO;
    }

    @Override
    public Inventory saveChildren(Inventory object) {
        upsert(object.getParent(), inventoryDAO);
        upsert(object.getCategory(), categoryDAO);
        for (Metric metric : object.getMetrics()) {
            upsert(metric, metricDAO);
        }
        return object;
    }
}

