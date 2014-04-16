package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;

import javax.inject.Inject;

public class MetricSaver extends Saver<Metric> {

    private final DAO<MetricType> dao;

    @Inject
    public MetricSaver(DAO<MetricType> dao) {
        this.dao = dao;
    }

    @Override
    public Metric saveChildren(Metric object) {
        object.setType(upsert(object.getType(), dao));
        return object;
    }
}
