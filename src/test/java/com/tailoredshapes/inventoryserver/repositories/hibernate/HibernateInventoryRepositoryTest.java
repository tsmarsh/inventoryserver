package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryInventoryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.metadata.ClassMetadata;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.tailoredshapes.inventoryserver.GuiceTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateInventoryRepositoryTest {
    private DAO<Inventory> inventoryDAO;
    private InMemoryInventoryRepository repo;
    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void setUp() throws Exception {
        repo = hibernateInjector.getInstance(new Key<InMemoryInventoryRepository>() {});
        inventoryDAO = hibernateInjector.getInstance(new Key<DAO<Inventory>>() {});
        sessionFactory = hibernateInjector.getInstance(SessionFactory.class);
    }

    @Test
    public void testFindById() throws Exception {
        session = sessionFactory.getCurrentSession();
        Map<String,ClassMetadata> allClassMetadata = sessionFactory.getAllClassMetadata();

        Transaction transaction = session.beginTransaction();
        User user = new UserBuilder().id(null).build();
        Inventory inventory = new InventoryBuilder().user(user).build();
        Inventory savedInventory = inventoryDAO.create(inventory);
        Inventory byId = repo.findById(savedInventory.getId());
        assertEquals(savedInventory, byId);
        transaction.rollback();
    }
}
