package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.tailoredshapes.inventoryserver.HibernateTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateMetricTypeRepositoryTest {
    private SimpleScope scope;

    @Before
    public void setUp() throws Exception {
        scope = hibernateInjector.getInstance(SimpleScope.class);
        scope.enter();
    }

    @After
    public void tearDown() throws Exception {
        scope.exit();
    }

    @Test
    public void testFindByName() throws Exception {
        EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        MetricType type = new MetricType().setName("Face");
        DAO<MetricType> metricTypeDAO = hibernateInjector.getInstance(new Key<DAO<MetricType>>() {});
        Repository<MetricType, EntityManager> repo = hibernateInjector.getInstance(new Key<Repository<MetricType, EntityManager>>(){});
        FinderFactory<MetricType, String, EntityManager> byFullName= hibernateInjector.getInstance(new Key<FinderFactory<MetricType, String, EntityManager>>(){});

        MetricType metricType = metricTypeDAO.create(type);

        MetricType byId = repo.findBy(byFullName.lookFor(metricType.getName()));

        assertEquals(metricType, byId);

        transaction.rollback();
    }

    @Test
    public void testMissByName() throws Exception {
        EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        Repository<MetricType, EntityManager> repo = hibernateInjector.getInstance(new Key<Repository<MetricType, EntityManager>>(){});
        FinderFactory<MetricType, String, EntityManager> byFullName= hibernateInjector.getInstance(new Key<FinderFactory<MetricType, String, EntityManager>>(){});


        MetricType byId = repo.findBy(byFullName.lookFor("archer"));
        assertEquals("archer", byId.getName());

        transaction.rollback();
    }
}
