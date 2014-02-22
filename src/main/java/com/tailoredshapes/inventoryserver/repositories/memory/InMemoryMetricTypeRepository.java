package com.tailoredshapes.inventoryserver.repositories.memory;

import com.tailoredshapes.inventoryserver.dao.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;

import javax.inject.Inject;
import java.util.Map;

public class InMemoryMetricTypeRepository implements MetricTypeRepository {

    private InMemoryDAO<MetricType> dao;

    @Inject
    public InMemoryMetricTypeRepository(InMemoryDAO<MetricType> dao) {
        this.dao = dao;
    }

    @Override
    public MetricType findByName(User user, String name) {
        Map<Long,MetricType> longMetricTypeMap = dao.db.get(user);
        for(MetricType type : longMetricTypeMap.values()){
            if(type.getName().equals(name)){
                return type;
            }
        }
        return null;
    }
}
