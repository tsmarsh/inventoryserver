package com.tailoredshapes.inventoryserver.dao.memory;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.security.Algorithm;

public class InMemoryMetricDAO<R extends Algorithm> extends InMemoryDAO<Metric, R> {

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
