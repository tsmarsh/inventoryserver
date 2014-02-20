package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.model.MetricType;

/**
 * Created by tmarsh on 2/17/14.
 */
public interface MetricTypeRepository {
    MetricType findByName(String name);
}
