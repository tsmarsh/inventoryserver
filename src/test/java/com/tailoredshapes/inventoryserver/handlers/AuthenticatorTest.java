package com.tailoredshapes.inventoryserver.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryUserRepository;
import com.tailoredshapes.inventoryserver.utils.KeyProvider;
import com.tailoredshapes.inventoryserver.utils.RSAKeyProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticatorTest {

    @Mock
    private HttpExchange testExchange;

    private UserRepository userRepository;

    @Mock
    private IdExtractor userIdExtractor;

    private User testUser;
    private UserDAO dao;
    private Serialiser<User> serialiser;
    private Encoder encoder;
    private KeyProvider keyprovider;

    @Before
    public void setUp() throws Exception {
        testUser = new UserBuilder().build();
        serialiser = new JSONSerialiser<>();
        encoder = new RSAEncoder();
        keyprovider = new RSAKeyProvider();

        dao = new InMemoryUserDAO(serialiser, encoder, keyprovider);
        testUser = dao.create(testUser);
        when(userIdExtractor.extract(testExchange)).thenReturn(testUser.getId());

        userRepository = new InMemoryUserRepository(dao);

    }

    @Test
    public void testShouldReturnTheUserForTheExchange() throws Exception {
        Authenticator authenticator = new Authenticator(userRepository, userIdExtractor);
        User authenticatedUser = authenticator.authenticate(testExchange);
        assertEquals(testUser, authenticatedUser);
    }
}
