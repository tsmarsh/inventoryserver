package com.tailoredshapes.inventoryserver.repositories.memory;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.Encoder;
import com.tailoredshapes.inventoryserver.dao.InMemoryChildFreeDAO;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.utils.KeyProvider;
import com.tailoredshapes.inventoryserver.utils.TestAlgorithm;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;

import static org.junit.Assert.assertEquals;

public class InMemoryUserRepositoryTest {

    Long testId = 1l;
    private byte[] testSerializedUser = new byte[0];

    private Serialiser<User> serialiser = object -> testSerializedUser;

    private Encoder<User, TestAlgorithm> encoder = (user) -> testId;

    private KeyProvider<TestAlgorithm> keyprovider = () -> new KeyPair(null, null);

    protected DAO<User> dao;
    private User storedUser;

    @Before
    public void setUp() throws Exception {
        dao = new InMemoryChildFreeDAO<>(encoder);
        storedUser = dao.create(new User());

    }

    @Test
    public void shouldFindUserById() throws Exception {
        InMemoryUserRepository inMemoryUserDAO = new InMemoryUserRepository(dao);

        User byId = inMemoryUserDAO.findById(1l);
        assertEquals(storedUser, byId);
    }
}
