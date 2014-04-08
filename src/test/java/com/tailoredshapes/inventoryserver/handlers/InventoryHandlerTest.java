package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static com.tailoredshapes.inventoryserver.HibernateTest.hibernateInjector;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InventoryHandlerTest {
    @Mock
    private HttpExchange createExchange;
    @Mock
    HttpExchange readExchange1;
    @Mock
    HttpExchange updateExchange;
    @Mock
    HttpExchange readExchange2;

    @Test
    public void testCanHandleInventoryRequestsInMemory() throws Exception {
        testCanCreateAnInventory(GuiceTest.injector);
    }

    @Test
    public void testCanHandleInventoryRequestsInHibernate() throws Exception {
        EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();
        testCanCreateAnInventory(hibernateInjector);
        transaction.rollback();
    }

    public void testCanCreateAnInventory(Injector injector) throws Exception {
        InventoryHandler handler;
        OutputStream stringStream;
        Map<String, String> parameters;
        Headers headers;
        JSONObject createResponseObject;
        String location;

        SimpleScope scope = injector.getInstance(SimpleScope.class);

        scope.enter();

        try {
            User user = new UserBuilder().build();

            scope.seed(Key.get(User.class, Names.named("current_user")), user);

            DAO<User> userDAO = injector.getInstance(new Key<DAO<User>>() {});
            Serialiser<Metric, String> metricSerialiser = injector.getInstance(new Key<Serialiser<Metric, String>>() {});

            user = userDAO.create(user);

            URI uri = new URI(String.format("http://localhost:80/users/%s/inventories", user.getId()));

            //CREATE
            handler = injector.getInstance(InventoryHandler.class);
            UrlBuilder<User> userUrlBuilder = injector.getInstance(new Key<UrlBuilder<User>>() {});
            UrlBuilder<Inventory> inventoryUrlBuilder = injector.getInstance(new Key<UrlBuilder<Inventory>>() {});

            stringStream = new ByteArrayOutputStream();
            parameters = new HashMap<>();

            headers = new Headers();
            when(createExchange.getResponseBody()).thenReturn(stringStream);
            when(createExchange.getAttribute("parameters")).thenReturn(parameters);
            when(createExchange.getResponseHeaders()).thenReturn(headers);
            when(createExchange.getRequestURI()).thenReturn(uri);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("category", "com.tailoredshapes.test");
            jsonObject.put("user", userUrlBuilder.build(user));
            parameters.put("inventory", jsonObject.toString());

            when(createExchange.getRequestMethod()).thenReturn("post");
            handler.handle(createExchange);
            verify(createExchange).sendResponseHeaders(eq(302), anyInt());
            assertTrue(headers.containsKey("location"));
            location = headers.get("location").get(0);

            createResponseObject = new JSONObject(stringStream.toString());
            assertEquals("com.tailoredshapes.test", createResponseObject.getString("category"));
            assertEquals(0, createResponseObject.getJSONArray("metrics").length());
            assertFalse(createResponseObject.has("parent"));
            assertNotNull(createResponseObject.getString("id"));

            //READ

            stringStream = new ByteArrayOutputStream();

            when(readExchange1.getRequestMethod()).thenReturn("get");
            when(readExchange1.getResponseBody()).thenReturn(stringStream);
            when(readExchange1.getRequestURI()).thenReturn(new URI(location));

            handler.handle(readExchange1);
            verify(readExchange1).sendResponseHeaders(eq(200), anyInt());

            JSONObject getResponseObject = new JSONObject(stringStream.toString());
            assertEquals(createResponseObject.getString("id"), getResponseObject.getString("id"));
            assertEquals(createResponseObject.getString("category"), getResponseObject.getString("category"));
            assertEquals(createResponseObject.getJSONArray("metrics").length(), getResponseObject.getJSONArray("metrics").length());

            //Update

            stringStream = new ByteArrayOutputStream();
            parameters = new HashMap<>();

            headers = new Headers();
            when(updateExchange.getResponseBody()).thenReturn(stringStream);
            when(updateExchange.getAttribute("parameters")).thenReturn(parameters);
            when(updateExchange.getResponseHeaders()).thenReturn(headers);
            when(updateExchange.getRequestURI()).thenReturn(new URI(location));

            JSONObject updatedObject = new JSONObject(getResponseObject.toString());
            JSONArray metrics = updatedObject.getJSONArray("metrics");
            Metric metric = new MetricBuilder().id(null).build();
            JSONObject metricJson = new JSONObject(metricSerialiser.serialise(metric));
            metrics.put(metricJson);

            parameters.put("inventory", updatedObject.toString());

            when(updateExchange.getRequestMethod()).thenReturn("post");
            handler.handle(updateExchange);
            verify(updateExchange).sendResponseHeaders(eq(302), anyInt());
            assertTrue(headers.containsKey("location"));
            assertNotSame(location, headers.get("location").get(0));

            JSONObject updateResponseObject = new JSONObject(stringStream.toString());
            assertEquals("com.tailoredshapes.test", updateResponseObject.getString("category"));
            assertEquals(1, updateResponseObject.getJSONArray("metrics").length());
            assertFalse(updateResponseObject.has("parent"));
            assertNotNull(updateResponseObject.getString("id"));

        } finally {
            scope.exit();
        }
    }
}
