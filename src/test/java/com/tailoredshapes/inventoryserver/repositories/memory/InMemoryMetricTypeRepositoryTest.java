package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.builders.MetricTypeBuilder;
import com.tailoredshapes.inventoryserver.security.SHA;
import org.junit.Test;

import static com.tailoredshapes.inventoryserver.GuiceTest.injector;
import static org.junit.Assert.assertEquals;

public class InMemoryMetricTypeRepositoryTest {

    @Test
    public void testFindByName() throws Exception {
        MetricType type = new MetricTypeBuilder().build();
        DAO<MetricType> metricTypeDAO = injector.getInstance(new Key<DAO<MetricType>>() {});
        InMemoryMetricTypeRepository repo = injector.getInstance(new Key<InMemoryMetricTypeRepository<SHA>>() {});
        MetricType metricType = metricTypeDAO.create(type);
        MetricType byId = repo.findByName(metricType.getName());
        assertEquals(metricType, byId);
    }
}
