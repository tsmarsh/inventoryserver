package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;

public interface MetricTypeRepository {
    MetricType findByName(String name);
}

