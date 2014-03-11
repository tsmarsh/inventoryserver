package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;

public class MetricSaver extends Saver<Metric> {

    private DAO<MetricType> dao;

    @Inject
    public MetricSaver(DAO<MetricType> dao) {
        this.dao = dao;
    }

    @Override
    public Metric saveChildren(Metric object) {
        upsert(object.getType(), dao);
        return object;
    }
}
