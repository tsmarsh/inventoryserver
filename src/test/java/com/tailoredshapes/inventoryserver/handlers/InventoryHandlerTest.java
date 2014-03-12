package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Key;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InventoryHandlerTest {

    private InventoryHandler handler;
    @Mock
    private HttpExchange createExchange;

    private DAO<User> userDAO = GuiceTest.injector.getInstance(new Key<DAO<User>>() {});
    private Serialiser<Metric> metricSerialiser = GuiceTest.injector.getInstance(new Key<Serialiser<Metric>>() {});

    @Mock
    HttpExchange readExchange1;
    @Mock
    HttpExchange updateExchange;
    @Mock
    HttpExchange readExchange2;

    private OutputStream stringStream;
    private Map<String, String> parameters;
    private Headers headers;
    private JSONObject createResponseObject;
    private String location;

    @Test
    public void testCanCreateAnInventory() throws Exception {

        User user = userDAO.create(new UserBuilder().build());
        URI uri = new URI(String.format("http://localhost:80/users/%s/inventories", user.getId()));

        //CREATE
        handler = GuiceTest.injector.getInstance(InventoryHandler.class);
        UrlBuilder<User> userUrlBuilder = GuiceTest.injector.getInstance(new Key<UrlBuilder<User>>() {});
        UrlBuilder<Inventory> inventoryUrlBuilder = GuiceTest.injector.getInstance(new Key<UrlBuilder<Inventory>>() {});

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
        assertEquals(userUrlBuilder.build(user), createResponseObject.getString("user"));
        assertEquals(0, createResponseObject.getJSONArray("metrics").length());
        assertFalse(createResponseObject.has("parent"));
        assertNotNull(createResponseObject.getLong("id"));

        //READ

        stringStream = new ByteArrayOutputStream();

        when(readExchange1.getRequestMethod()).thenReturn("get");
        when(readExchange1.getResponseBody()).thenReturn(stringStream);
        when(readExchange1.getRequestURI()).thenReturn(new URI(location));

        handler.handle(readExchange1);
        verify(readExchange1).sendResponseHeaders(eq(200), anyInt());

        JSONObject getResponseObject = new JSONObject(stringStream.toString());
        assertEquals(createResponseObject.getLong("id"), getResponseObject.getLong("id"));
        assertEquals(createResponseObject.getString("category"), getResponseObject.getString("category"));
        assertEquals(createResponseObject.getJSONArray("metrics").length(), getResponseObject.getJSONArray("metrics").length());
        assertEquals(createResponseObject.getString("user"), getResponseObject.getString("user"));

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
        JSONObject metricJson = new JSONObject(new String(metricSerialiser.serialise(metric)));
        metrics.put(metricJson);

        parameters.put("inventory", updatedObject.toString());

        when(updateExchange.getRequestMethod()).thenReturn("post");
        handler.handle(updateExchange);
        verify(updateExchange).sendResponseHeaders(eq(302), anyInt());
        assertTrue(headers.containsKey("location"));
        assertNotSame(location, headers.get("location").get(0));

        JSONObject updateResponseObject = new JSONObject(stringStream.toString());
        assertEquals("com.tailoredshapes.test", updateResponseObject.getString("category"));
        assertEquals(userUrlBuilder.build(user), updateResponseObject.getString("user"));
        assertEquals(1, updateResponseObject.getJSONArray("metrics").length());
        assertFalse(updateResponseObject.has("parent"));
        assertNotNull(updateResponseObject.getLong("id"));
    }
}
