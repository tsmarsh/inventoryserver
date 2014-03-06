package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.utils.Algorithm;

public class InMemoryInventoryDAO<R extends Algorithm> extends InMemoryDAO<Inventory, R> {

    private final DAO<User> userDAO;
    private final DAO<Metric> metricDAO;
    private DAO<Category> categoryDAO;

    @Inject
    public InMemoryInventoryDAO(DAO<User> userDAO,
                                DAO<Metric> metricDAO,
                                DAO<Category> categoryDAO,
                                Encoder<Inventory, R> encoder) {
        super(encoder);
        this.userDAO = userDAO;
        this.metricDAO = metricDAO;
        this.categoryDAO = categoryDAO;
    }

    @Override
    public Inventory saveChildren(Inventory object) {
        upsert(object.getUser(), userDAO);
        upsert(object.getParent(), this);
        upsert(object.getCategory(), categoryDAO);
        for(Metric metric : object.getMetrics()){
            upsert(metric, metricDAO);
        }
        return object;
    }
}

