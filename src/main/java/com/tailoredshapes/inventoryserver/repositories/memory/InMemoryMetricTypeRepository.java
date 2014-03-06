package com.tailoredshapes.inventoryserver.repositories.memory;

import com.tailoredshapes.inventoryserver.dao.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import com.tailoredshapes.inventoryserver.utils.Algorithm;

import javax.inject.Inject;

public class InMemoryMetricTypeRepository<R extends Algorithm> implements MetricTypeRepository {

    private final InMemoryDAO<MetricType, R> dao;

    @Inject
    public InMemoryMetricTypeRepository(InMemoryDAO<MetricType, R> dao) {
        this.dao = dao;
    }

    @Override
    public MetricType findByName(String name) {

        for (MetricType type : dao.db.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }

        return new MetricType().setName(name);
    }
}
