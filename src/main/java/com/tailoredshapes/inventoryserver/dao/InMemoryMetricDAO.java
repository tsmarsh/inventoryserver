package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.utils.Algorithm;

public class InMemoryMetricDAO<R extends Algorithm> extends InMemoryDAO<Metric, R>{

    private DAO<MetricType> dao;

    @Inject
    public InMemoryMetricDAO(DAO<MetricType> dao, Encoder<Metric, R> encoder) {
        super(encoder);
        this.dao = dao;
    }

    @Override
    public Metric saveChildren(Metric object) {
        upsert(object.getType(), dao);
        return object;
    }
}
