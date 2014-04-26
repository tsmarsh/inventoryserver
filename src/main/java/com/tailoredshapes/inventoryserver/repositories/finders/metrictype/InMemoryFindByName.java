package com.tailoredshapes.inventoryserver.repositories.finders.metrictype;

import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;

import java.util.Map;

public class InMemoryFindByName implements FinderFactory<MetricType, String, Map<Long, MetricType>>, Finder<MetricType, Map<Long, MetricType>>{
    private String name;

    @Override
    public Finder<MetricType, Map<Long, MetricType>> lookFor(String... strings) {
        this.name = strings[0];
        return this;
    }

    @Override
    public MetricType find(Map<Long, MetricType> db) {
        for (MetricType type : db.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }

        return new MetricType().setName(name);
    }
}
