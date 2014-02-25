package com.tailoredshapes.inventoryserver.repositories.memory;

import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.MetricTypeBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InMemoryMetricTypeRepositoryTest {


    private Serialiser<MetricType> serialiser;
    private Encoder encoder;
    private InMemoryDAO<MetricType> dao;

    @Before
    public void setUp() throws Exception {
        serialiser = new JSONSerialiser<>();
        encoder = new RSAEncoder();
        dao = new InMemoryDAO<>(serialiser, encoder);
    }

    @Test
    public void testFindByName() throws Exception {
        User user = new UserBuilder().build();
        MetricType category = new MetricTypeBuilder().build();
        InMemoryMetricTypeRepository repo = new InMemoryMetricTypeRepository(dao);
        MetricType savedCategory = dao.create(user, category);
        MetricType byId = repo.findByName(user, savedCategory.getName());
        assertEquals(savedCategory, byId);
    }
}
