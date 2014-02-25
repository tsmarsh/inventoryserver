package com.tailoredshapes.inventoryserver;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InventoryServerTest {

    private Injector injector = Guice.createInjector(new InventoryModule("0.0.0.0", 5555));
    private HttpServer server;
    private HttpClient httpClient = new HttpClient();

    @Before
    public void setUp() throws Exception {
        server = injector.getInstance(HttpServer.class);
        server.start();

    }

    @Test
    public void testCanCRUDAUser() throws Exception {
        //CREATE
        PostMethod postMethod = new PostMethod("http://localhost:5555/users");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Archer");

        RequestEntity entity = new StringRequestEntity(jsonObject.toString());
        postMethod.setRequestEntity(entity);
        int responseCode = httpClient.executeMethod(postMethod);
        assertEquals(302, responseCode);
        String location = postMethod.getResponseHeader("location").getValue();
        assertNotNull(location);
        JSONObject putResponseObject = new JSONObject(new String(postMethod.getResponseBody()));
        assertEquals("Archer", putResponseObject.getString("name"));
        assertNotNull(putResponseObject.getLong("id"));
        assertNotNull(putResponseObject.getString("publicKey"));
        assertNull(putResponseObject.getString("privateKey"));


        //READ
        GetMethod getMethod = new GetMethod(location);
        responseCode = httpClient.executeMethod(getMethod);
        assertEquals(200, responseCode);
        JSONObject getResponseObject = new JSONObject(new String(postMethod.getResponseBody()));
        assertEquals("Archer", getResponseObject.getString("name"));
        assertEquals(putResponseObject.getLong("id"), getResponseObject.getLong("id"));
        assertEquals(putResponseObject.getString("publicKey"), getResponseObject.getString("publicKey"));

        //UPDATE / DELETE
        postMethod = new PostMethod(location);
        putResponseObject.put("name", "Cassie");
        RequestEntity postEntity = new StringRequestEntity(jsonObject.toString());
        postMethod.setRequestEntity(postEntity);
        responseCode = httpClient.executeMethod(postMethod);
        assertEquals(302, responseCode);
        location = postMethod.getResponseHeader("location").getValue();
        assertNotNull(location);

        JSONObject postResponseObject = new JSONObject(new String(postMethod.getResponseBody()));
        assertEquals("Cassie", getResponseObject.getString("name"));
        assertNotSame(postResponseObject.getLong("id"), getResponseObject.getLong("id"));
        assertEquals(postResponseObject.getString("publicKey"), getResponseObject.getString("publicKey"));

        //READ
        getMethod = new GetMethod(location);
        responseCode = httpClient.executeMethod(getMethod);
        assertEquals(200, responseCode);
        getResponseObject = new JSONObject(new String(postMethod.getResponseBody()));
        assertEquals("Cassie", getResponseObject.getString("name"));
        assertEquals(putResponseObject.getLong("id"), getResponseObject.getLong("id"));
        assertEquals(putResponseObject.getString("publicKey"), getResponseObject.getString("publicKey"));
    }
}
