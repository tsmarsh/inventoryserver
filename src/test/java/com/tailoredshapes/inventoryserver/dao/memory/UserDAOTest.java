package com.tailoredshapes.inventoryserver.dao.memory;

import com.google.inject.Key;
import com.google.inject.persist.Transactional;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDAOTest extends GuiceTest {

    @Test
    public void testInMemory() throws Exception {
        DAO<User> dao = GuiceTest.injector.getInstance(new Key<DAO<User>>() {});
        testSaveChildren(dao);
    }

    @Test
    public void testHibernate() throws Exception {
        EntityManager instance = GuiceTest.hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = instance.getTransaction();
        transaction.begin();
        DAO<User> dao = GuiceTest.hibernateInjector.getInstance(new Key<DAO<User>>() {});
        testSaveChildren(dao);
        transaction.rollback();
    }

    private void testSaveChildren(DAO<User> dao) throws Exception {
        dao = injector.getInstance(new Key<DAO<User>>() {});
        User user = new User().setName("Archer");
        User savedUser = dao.create(user);

        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getPublicKey());
        assertNotNull(savedUser.getPrivateKey());

        User readUser = dao.read(new User().setId(savedUser.getId()));
        assertEquals(savedUser, readUser);
    }
}
