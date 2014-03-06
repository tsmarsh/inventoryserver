package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.model.MetricType;

public interface MetricTypeRepository {
    MetricType findByName(String name);
}

