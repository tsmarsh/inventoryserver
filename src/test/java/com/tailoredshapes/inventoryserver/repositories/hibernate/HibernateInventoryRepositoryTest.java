package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryInventoryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.tailoredshapes.inventoryserver.GuiceTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateInventoryRepositoryTest {
    private DAO<Inventory> inventoryDAO;
    private InMemoryInventoryRepository repo;

    @Test
    public void testFindById() throws Exception {
        repo = hibernateInjector.getInstance(new Key<InMemoryInventoryRepository>() {});
        inventoryDAO = hibernateInjector.getInstance(new Key<DAO<Inventory>>() {});
        EntityManager em = hibernateInjector.getInstance(EntityManager.class);

        EntityTransaction transaction = em.getTransaction();
        try{
            transaction.begin();
            Inventory inventory = new InventoryBuilder().build();
            Inventory savedInventory = inventoryDAO.create(inventory);
            Inventory byId = repo.findById(savedInventory.getId());
            assertEquals(savedInventory, byId);
        }finally {
            transaction.rollback();
        }

    }
}
