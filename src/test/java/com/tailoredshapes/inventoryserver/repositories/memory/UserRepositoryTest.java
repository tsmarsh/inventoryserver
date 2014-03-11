package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;

import static com.tailoredshapes.inventoryserver.GuiceTest.injector;
import static org.junit.Assert.assertEquals;

public class UserRepositoryTest {

    protected DAO<User> dao;
    private User storedUser;

    @Before
    public void setUp() throws Exception {
        dao = injector.getInstance(new Key<DAO<User>>() {});
        storedUser = dao.create(new User());
    }

    @Test
    public void shouldFindUserById() throws Exception {
        UserRepository inMemoryUserDAO = injector.getInstance(UserRepository.class);

        User byId = inMemoryUserDAO.findById(storedUser.getId());
        assertEquals(storedUser, byId);
    }
}
