package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.HibernateTest;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.tailoredshapes.inventoryserver.GuiceTest.injector;
import static org.junit.Assert.assertEquals;

public class UserRepositoryTest {

    protected DAO<User> dao;
    private User storedUser;
    private SimpleScope scope;

    @Test
    public void testInMemory() throws Exception {
        shouldFindUserById(GuiceTest.injector, new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Test
    public void testHibernate() throws Exception {
        final EntityManager manager = HibernateTest.hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        shouldFindUserById(HibernateTest.hibernateInjector, new Runnable() {
            @Override
            public void run() {

                manager.flush();
                manager.clear();
            }
        });
        transaction.rollback();
    }

    public void shouldFindUserById(Injector injector, Runnable flusher) throws Exception {
        scope = injector.getInstance(SimpleScope.class);
        scope.enter();
        try{
            scope.seed(Key.get(User.class, Names.named("current_user")), new User());
            dao = injector.getInstance(new Key<DAO<User>>() {});
            storedUser = dao.create(new User().setName("Archer"));

            flusher.run();

            UserRepository repository = injector.getInstance(UserRepository.class);

            User byId = repository.findById(storedUser.getId());
            assertEquals(storedUser, byId);
        }finally {
            scope.exit();
        }
    }
}
