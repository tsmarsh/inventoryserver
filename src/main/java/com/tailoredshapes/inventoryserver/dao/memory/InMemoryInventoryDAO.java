package com.tailoredshapes.inventoryserver.dao.memory;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.security.Algorithm;

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
        for (Metric metric : object.getMetrics()) {
            upsert(metric, metricDAO);
        }
        return object;
    }
}

