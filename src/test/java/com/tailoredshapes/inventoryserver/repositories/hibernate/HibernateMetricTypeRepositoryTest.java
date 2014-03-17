package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;

import static com.tailoredshapes.inventoryserver.GuiceTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateMetricTypeRepositoryTest {
    @Test
    public void testFindByName() throws Exception {
        SessionFactory instance = hibernateInjector.getInstance(SessionFactory.class);
        Session currentSession = instance.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

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
        SessionFactory instance = hibernateInjector.getInstance(SessionFactory.class);
        Session currentSession = instance.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        MetricTypeRepository repo = hibernateInjector.getInstance(MetricTypeRepository.class);

        MetricType byId = repo.findByName("archer");
        assertEquals("archer", byId.getName());

        transaction.rollback();
    }
}
