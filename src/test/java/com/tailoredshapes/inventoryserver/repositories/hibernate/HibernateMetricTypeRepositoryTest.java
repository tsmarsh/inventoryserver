package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.tailoredshapes.inventoryserver.HibernateTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateMetricTypeRepositoryTest {
    @Test
    public void testFindByName() throws Exception {
        EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        MetricType type = new MetricType().setName("Face");
        DAO<MetricType> metricTypeDAO = hibernateInjector.getInstance(new Key<DAO<MetricType>>() {});
        MetricTypeRepository repo = hibernateInjector.getInstance(MetricTypeRepository.class);
        MetricType metricType = metricTypeDAO.create(type);

        MetricType byId = repo.findByName(metricType.getName());
        assertEquals(metricType, byId);

        transaction.rollback();
    }

    @Test
    public void testMissByName() throws Exception {
        EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        MetricTypeRepository repo = hibernateInjector.getInstance(MetricTypeRepository.class);

        MetricType byId = repo.findByName("archer");
        assertEquals("archer", byId.getName());

        transaction.rollback();
    }
}
