package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.tailoredshapes.inventoryserver.HibernateTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateInventoryRepositoryTest {
    private DAO<Inventory> inventoryDAO;
    private Repository<Inventory> repo;

    private SimpleScope scope;

    @Before
    public void setUp() throws Exception {
        scope = hibernateInjector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), new User());
    }

    @After
    public void tearDown() throws Exception {
        scope.exit();
    }

    @Test
    public void testFindById() throws Exception {
        repo = hibernateInjector.getInstance(new Key<Repository<Inventory>>() {});
        inventoryDAO = hibernateInjector.getInstance(new Key<DAO<Inventory>>() {});
        EntityManager em = hibernateInjector.getInstance(EntityManager.class);

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Inventory inventory = new InventoryBuilder().build();
            Inventory savedInventory = inventoryDAO.create(inventory);
            em.flush();
            em.clear();
            Inventory byId = repo.findById(savedInventory.getId());
            assertEquals(savedInventory, byId);
        } finally {
            transaction.rollback();
        }

    }
}
