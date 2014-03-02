package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.InventoryModule;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.UserDAO;
import com.tailoredshapes.inventoryserver.dao.UserSerialiser;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
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
import static org.mockito.Matchers.intThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InventoryHandlerTest{

    private Injector injector = Guice.createInjector(new InventoryModule("localhost", 6666));
    private InventoryHandler handler;
    @Mock
    private HttpExchange createExchange;

    private UserDAO userDAO = injector.getInstance(UserDAO.class);
    @Mock
    HttpExchange readExchange1;
    @Mock
    HttpExchange updateExchange;
    @Mock
    HttpExchange readExchange2;

    private OutputStream stringStream;
    private Map<String, String> parameters;
    private Headers headers;

    @Test
    public void testCanCreateAnInventory() throws Exception {

        User user = userDAO.create(new UserBuilder().build());
        URI uri = new URI(String.format("http://localhost:80/%s/inventories", user.getId()));

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
        jsonObject.put("user_id", user.getId());
        parameters.put("inventory", jsonObject.toString());

        when(createExchange.getRequestMethod()).thenReturn("post");
        handler.handle(createExchange);
        verify(createExchange).sendResponseHeaders(eq(302), anyInt());
        assertTrue(headers.containsKey("location"));
        String location = headers.get("location").get(0);

        JSONObject putResponseObject = new JSONObject(stringStream.toString());
        assertEquals("com.tailoredshapes.test", putResponseObject.getString("category"));
        assertEquals(userUrlBuilder.build(user), putResponseObject.getString("user"));
        assertEquals(0, putResponseObject.getJSONArray("metrics").length());
        assertFalse(putResponseObject.has("parent"));
        assertNotNull(putResponseObject.getLong("id"));

    //        //READ
//        stringStream = new ByteArrayOutputStream();
//        parameters.put("user", putResponseObject.toString());
//        when(readExchange1.getRequestMethod()).thenReturn("get");
//        when(readExchange1.getAttribute("parameters")).thenReturn(parameters);
//        when(readExchange1.getResponseBody()).thenReturn(stringStream);
//
//        handler.handle(readExchange1);
//        verify(readExchange1).sendResponseHeaders(eq(200), anyInt());
//
//        JSONObject getResponseObject = new JSONObject(stringStream.toString());
//        assertEquals("Archer", getResponseObject.getString("name"));
//        assertEquals(putResponseObject.getLong("id"), getResponseObject.getLong("id"));
//        assertEquals(putResponseObject.getString("publicKey"), getResponseObject.getString("publicKey"));
//        assertFalse(putResponseObject.has("privateKey"));


//        //UPDATE / DELETE
//        stringStream = new ByteArrayOutputStream();
//        headers = new Headers();
//        putResponseObject.put("name", "Cassie");
//        parameters.put("user", putResponseObject.toString());
//
//        when(updateExchange.getRequestMethod()).thenReturn("post");
//        when(updateExchange.getAttribute("parameters")).thenReturn(parameters);
//        when(updateExchange.getResponseBody()).thenReturn(stringStream);
//        when(updateExchange.getResponseHeaders()).thenReturn(headers);
//
//        handler.handle(updateExchange);
//        verify(updateExchange).sendResponseHeaders(eq(302), anyInt());
//
//        String location2 = headers.get("location").get(0);
//        assertNotNull(location2);
//        assertNotSame(location, location2);
//
//
//        JSONObject postResponseObject = new JSONObject(stringStream.toString());
//        assertEquals("Cassie", postResponseObject.getString("name"));
//        assertNotSame(postResponseObject.getLong("id"), getResponseObject.getLong("id"));
//        assertEquals(postResponseObject.getString("publicKey"), getResponseObject.getString("publicKey"));
//
//        //Read
//
//        stringStream = new ByteArrayOutputStream();
//        parameters.put("user", putResponseObject.toString());
//        when(readExchange2.getRequestMethod()).thenReturn("get");
//        when(readExchange2.getAttribute("parameters")).thenReturn(parameters);
//        when(readExchange2.getResponseBody()).thenReturn(stringStream);
//
//        handler.handle(readExchange2);
//        verify(readExchange2).sendResponseHeaders(eq(200), anyInt());
//
//        JSONObject getResponseObject2 = new JSONObject(stringStream.toString());
//        assertEquals("Cassie", getResponseObject2.getString("name"));
//        assertEquals(postResponseObject.getLong("id"), getResponseObject2.getLong("id"));
//        assertEquals(postResponseObject.getString("publicKey"), getResponseObject2.getString("publicKey"));
//        assertFalse(postResponseObject.has("privateKey"));
    }

}
