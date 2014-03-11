package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Key;
import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticatorTest {

    @Mock
    private HttpExchange testExchange;

    private User testUser;
    private DAO<User> dao;

    @Before
    public void setUp() throws Exception {
        testUser = new User().setName("Archer");

        dao = GuiceTest.injector.getInstance(new Key<DAO<User>>() {});
        testUser = dao.create(testUser);
        when(testExchange.getRequestURI()).thenReturn(new URI("http://localhost:80/users/" + testUser.getId()));
    }

    @Test
    public void testShouldReturnTheUserForTheExchange() throws Exception {
        Authenticator authenticator = GuiceTest.injector.getInstance(Authenticator.class);
        User authenticatedUser = authenticator.authenticate(testExchange);
        assertEquals(testUser, authenticatedUser);
    }
}
