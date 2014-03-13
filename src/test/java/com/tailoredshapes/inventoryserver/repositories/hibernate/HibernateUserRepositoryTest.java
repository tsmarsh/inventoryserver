package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;

import static com.tailoredshapes.inventoryserver.GuiceTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateUserRepositoryTest {

    @Test
    public void shouldFindUserById() throws Exception {
        SessionFactory instance = hibernateInjector.getInstance(SessionFactory.class);
        Session currentSession = instance.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        DAO<User> dao = hibernateInjector.getInstance(new Key<DAO<User>>() {});
        User storedUser = dao.create(new User());
        UserRepository repository = hibernateInjector.getInstance(UserRepository.class);

        User byId = repository.findById(storedUser.getId());
        assertEquals(storedUser, byId);

        transaction.rollback();
    }
}
