package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Key;
import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UrlIdExtractorTest {

    @Mock
    HttpExchange exchange;
    private SimpleScope scope;

    @Before
    public void setUp() throws Exception {
        scope = GuiceTest.injector.getInstance(SimpleScope.class);
        scope.enter();
    }

    @After
    public void tearDown() throws Exception {
        scope.exit();
    }

    @Test
    public void testExtract() throws Exception {
        String path = "http://localhost:80/users/Archer/555/inventories/666";

        URI uri = new URI(path);
        when(exchange.getRequestURI()).thenReturn(uri);

        IdExtractor<Long, User> urlIdExtractor = GuiceTest.injector.getInstance(new Key<IdExtractor<Long, User>>() {});
        Long extract = urlIdExtractor.extract(exchange);
        assertThat(new Long(555l)).isEqualTo(extract);
    }

    @Test
    public void testExtractNegativeId() throws Exception {
        String path = "http://localhost:80/users/Archer/-555/inventories/666";

        URI uri = new URI(path);
        when(exchange.getRequestURI()).thenReturn(uri);

        IdExtractor<Long, User> urlIdExtractor = GuiceTest.injector.getInstance(new Key<IdExtractor<Long, User>>() {});
        Long extract = urlIdExtractor.extract(exchange);
        assertThat(new Long(-555l)).isEqualTo(extract);
    }

    @Test
    public void testExtractByName() throws Exception {
        String path = "http://localhost:80/users/Jasper";
        DAO<User> dao = GuiceTest.injector.getInstance(new Key<DAO<User>>() {});

        User archer = dao.create(new UserBuilder().id(null).name("Jasper").build());

        URI uri = new URI(path);
        when(exchange.getRequestURI()).thenReturn(uri);

        IdExtractor<String, User> urlIdExtractor = GuiceTest.injector.getInstance(new Key<IdExtractor<String, User>>() {});
        String extract = urlIdExtractor.extract(exchange);
        assertThat(archer.getName()).isEqualTo(extract);
    }
}

