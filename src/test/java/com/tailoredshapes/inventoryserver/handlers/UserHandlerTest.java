package com.tailoredshapes.inventoryserver.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.GuiceTest;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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

import static com.tailoredshapes.inventoryserver.GuiceTest.hibernateInjector;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserHandlerTest {

    private UserHandler handler;
    @Mock
    private HttpExchange exchange;

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
    public void testCanCRUDAUserInMemory() throws Exception {
        handler = GuiceTest.injector.getInstance(UserHandler.class);
        testCanCRUDAUser(handler);
    }

    @Test
    public void testCanCRUDAUserInHibernate() throws Exception {
        EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();

        handler = GuiceTest.hibernateInjector.getInstance(UserHandler.class);
        testCanCRUDAUser(handler);
        transaction.rollback();
    }

    private void testCanCRUDAUser(UserHandler handler) throws Exception {
        //CREATE

        stringStream = new ByteArrayOutputStream();
        parameters = new HashMap<>();

        headers = new Headers();
        when(exchange.getResponseBody()).thenReturn(stringStream);
        when(exchange.getAttribute("parameters")).thenReturn(parameters);
        when(exchange.getResponseHeaders()).thenReturn(headers);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Archer");
        parameters.put("user", jsonObject.toString());

        when(exchange.getRequestMethod()).thenReturn("post");
        handler.handle(exchange);
        verify(exchange).sendResponseHeaders(eq(302), anyInt());
        assertTrue(headers.containsKey("location"));
        String location = headers.get("location").get(0);

        JSONObject createResponseObject = new JSONObject(stringStream.toString());
        assertEquals("Archer", createResponseObject.getString("name"));
        assertNotNull(createResponseObject.getLong("id"));
        assertNotNull(createResponseObject.getString("publicKey"));
        assertFalse(createResponseObject.has("privateKey"));


        //READ
        stringStream = new ByteArrayOutputStream();
        parameters.put("user", createResponseObject.toString());
        when(readExchange1.getRequestMethod()).thenReturn("get");
        when(readExchange1.getAttribute("parameters")).thenReturn(parameters);
        when(readExchange1.getResponseBody()).thenReturn(stringStream);
        when(readExchange1.getRequestURI()).thenReturn(new URI(location));

        handler.handle(readExchange1);
        verify(readExchange1).sendResponseHeaders(eq(200), anyInt());

        JSONObject readCreateResponseObject = new JSONObject(stringStream.toString());
        assertEquals("Archer", readCreateResponseObject.getString("name"));
        assertEquals(createResponseObject.getLong("id"), readCreateResponseObject.getLong("id"));
        assertEquals(createResponseObject.getString("publicKey"), readCreateResponseObject.getString("publicKey"));
        assertFalse(createResponseObject.has("privateKey"));


        //UPDATE / DELETE
        stringStream = new ByteArrayOutputStream();
        headers = new Headers();
        createResponseObject.put("name", "Cassie");
        parameters.put("user", createResponseObject.toString());

        when(updateExchange.getRequestMethod()).thenReturn("post");
        when(updateExchange.getAttribute("parameters")).thenReturn(parameters);
        when(updateExchange.getResponseBody()).thenReturn(stringStream);
        when(updateExchange.getResponseHeaders()).thenReturn(headers);

        handler.handle(updateExchange);
        verify(updateExchange).sendResponseHeaders(eq(302), anyInt());

        String updatedLocation = headers.get("location").get(0);
        assertNotNull(updatedLocation);
        assertNotSame(location, updatedLocation);


        JSONObject updateResponseObject = new JSONObject(stringStream.toString());
        assertEquals("Cassie", updateResponseObject.getString("name"));
        assertNotSame(updateResponseObject.getLong("id"), readCreateResponseObject.getLong("id"));
        assertEquals(updateResponseObject.getString("publicKey"), readCreateResponseObject.getString("publicKey"));

        //Read

        stringStream = new ByteArrayOutputStream();
        parameters.put("user", createResponseObject.toString());
        when(readExchange2.getRequestMethod()).thenReturn("get");
        when(readExchange2.getAttribute("parameters")).thenReturn(parameters);
        when(readExchange2.getResponseBody()).thenReturn(stringStream);
        when(readExchange2.getRequestURI()).thenReturn(new URI(updatedLocation));

        handler.handle(readExchange2);
        verify(readExchange2).sendResponseHeaders(eq(200), anyInt());

        JSONObject readUpdateResponseObject = new JSONObject(stringStream.toString());
        assertEquals("Cassie", readUpdateResponseObject.getString("name"));
        assertEquals(updateResponseObject.getLong("id"), readUpdateResponseObject.getLong("id"));
        assertEquals(updateResponseObject.getString("publicKey"), readUpdateResponseObject.getString("publicKey"));
        assertFalse(updateResponseObject.has("privateKey"));
    }

}
