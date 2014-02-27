package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.tailoredshapes.inventoryserver.handlers.UserHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.runners.MockitoJUnitRunner;

import javax.xml.ws.spi.http.HttpHandler;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InventoryServerTest {

    private Injector injector = Guice.createInjector(new InventoryModule("localhost", 6666));
    private UserHandler handler;
    @Mock
    private HttpExchange exchange;

    @Mock HttpExchange readExchange1;
    @Mock HttpExchange updateExchange;
    @Mock HttpExchange readExchange2;

    private OutputStream stringStream;
    private Map<String, String> parameters;
    private Headers headers;

    @Before
    public void setUp() throws Exception {
        handler = injector.getInstance(UserHandler.class);
        stringStream = new ByteArrayOutputStream();
        parameters = new HashMap<>();

        headers = new Headers();
        when(exchange.getResponseBody()).thenReturn(stringStream);
        when(exchange.getAttribute("parameters")).thenReturn(parameters);
        when(exchange.getResponseHeaders()).thenReturn(headers);
    }

    @Test
    public void testCanCRUDAUser() throws Exception {


        //CREATE

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Archer");
        parameters.put("user", jsonObject.toString());

        when(exchange.getRequestMethod()).thenReturn("post");
        handler.handle(exchange);
        verify(exchange).sendResponseHeaders(eq(302), anyInt());
        assertTrue(headers.containsKey("location"));
        String location = headers.get("location").get(0);

        JSONObject putResponseObject = new JSONObject(stringStream.toString());
        assertEquals("Archer", putResponseObject.getString("name"));
        assertNotNull(putResponseObject.getLong("id"));
        assertNotNull(putResponseObject.getString("publicKey"));
        assertFalse(putResponseObject.has("privateKey"));


        //READ
        stringStream = new ByteArrayOutputStream();
        parameters.put("user", putResponseObject.toString());
        when(readExchange1.getRequestMethod()).thenReturn("get");
        when(readExchange1.getAttribute("parameters")).thenReturn(parameters);
        when(readExchange1.getResponseBody()).thenReturn(stringStream);

        handler.handle(readExchange1);
        verify(readExchange1).sendResponseHeaders(eq(200), anyInt());

        JSONObject getResponseObject = new JSONObject(stringStream.toString());
        assertEquals("Archer", getResponseObject.getString("name"));
        assertEquals(putResponseObject.getLong("id"), getResponseObject.getLong("id"));
        assertEquals(putResponseObject.getString("publicKey"), getResponseObject.getString("publicKey"));
        assertFalse(putResponseObject.has("privateKey"));


//        //UPDATE / DELETE
//        postMethod = new PostMethod(location);
//        putResponseObject.put("name", "Cassie");
//        RequestEntity postEntity = new StringRequestEntity(jsonObject.toString());
//        postMethod.setRequestEntity(postEntity);
//        responseCode = httpClient.executeMethod(postMethod);
//        assertEquals(302, responseCode);
//        location = postMethod.getResponseHeader("location").getValue();
//        assertNotNull(location);
//
//        JSONObject postResponseObject = new JSONObject(new String(postMethod.getResponseBody()));
//        assertEquals("Cassie", getResponseObject.getString("name"));
//        assertNotSame(postResponseObject.getLong("id"), getResponseObject.getLong("id"));
//        assertEquals(postResponseObject.getString("publicKey"), getResponseObject.getString("publicKey"));
//
//
//        //READ
//        getMethod = new GetMethod(location);
//        responseCode = httpClient.executeMethod(getMethod);
//        assertEquals(200, responseCode);
//        getResponseObject = new JSONObject(new String(postMethod.getResponseBody()));
//        assertEquals("Cassie", getResponseObject.getString("name"));
//        assertEquals(putResponseObject.getLong("id"), getResponseObject.getLong("id"));
//        assertEquals(putResponseObject.getString("publicKey"), getResponseObject.getString("publicKey"));
    }

}
