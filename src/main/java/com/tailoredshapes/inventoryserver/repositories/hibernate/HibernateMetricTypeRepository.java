package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;

public class HibernateMetricTypeRepository implements MetricTypeRepository {

    @Override
    public MetricType findByName(String name) {
        return null;
    }
}

