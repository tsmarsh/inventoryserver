package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.tailoredshapes.inventoryserver.HibernateTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateUserRepositoryTest {
    private SimpleScope scope;

    @Before
    public void setUp() throws Exception {
        scope = hibernateInjector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(User.class, new User());
    }

    @After
    public void tearDown() throws Exception {
        scope.exit();
    }

    @Test
    public void shouldFindUserById() throws Exception {
        EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();

        DAO<User> dao = hibernateInjector.getInstance(new Key<DAO<User>>() {});
        User storedUser = dao.create(new User());
        UserRepository repository = hibernateInjector.getInstance(UserRepository.class);

        User byId = repository.findById(storedUser.getId());
        assertEquals(storedUser, byId);

        transaction.rollback();
    }
}
