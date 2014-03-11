package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.security.Algorithm;

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
