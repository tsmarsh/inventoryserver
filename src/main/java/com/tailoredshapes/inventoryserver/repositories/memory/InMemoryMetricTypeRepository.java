package com.tailoredshapes.inventoryserver.repositories.memory;

import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import com.tailoredshapes.inventoryserver.security.Algorithm;

import javax.inject.Inject;
import java.util.Map;

public class InMemoryMetricTypeRepository<R extends Algorithm> implements MetricTypeRepository {

    private final Map<Long, MetricType> db;

    @Inject
    public InMemoryMetricTypeRepository(Map<Long, MetricType> db) {
        this.db = db;
    }

    @Override
    public MetricType findByName(String name) {

        for (MetricType type : db.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }

        return new MetricType().setName(name);
    }
}
