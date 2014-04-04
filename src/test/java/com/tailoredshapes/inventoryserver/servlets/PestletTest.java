package com.tailoredshapes.inventoryserver.servlets;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PestletTest {

    @Test
    public void testCanHandleInventoryRequestsInHibernate() throws Exception {
        int port = 7777;

        Server server = new Server(port);
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");

        webAppContext.setWar(this.getClass().getResource("/").getPath());
        server.setHandler(webAppContext);
        try {
            server.start();

            testCanCreateAnInventory(port);
        }
        finally {
            server.stop();
        }
    }

    public void testCanCreateAnInventory(Integer port) throws Exception {
        HttpClient httpClient = HttpClients.createDefault();

        //CREATE USER

        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "Archer");

        HttpPost userPost = new HttpPost(new URI(String.format("http://localhost:%d/users", port)));
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("user", userJsonObject.toString()));
        userPost.setEntity(new UrlEncodedFormEntity(parameters));
        HttpResponse userReponse = httpClient.execute(userPost);
        Header location = userReponse.getFirstHeader("Location");
        String userUrl = location.getValue();


        //CREATE

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("category", "com.tailoredshapes.test");
        jsonObject.put("user", userUrl);

        HttpPost httpPost = new HttpPost(new URI(String.format("%s/inventories", userUrl)));
        parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("inventory", jsonObject.toString()));
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));


        HttpResponse response = httpClient.execute(httpPost);
        assertEquals(302, response.getStatusLine().getStatusCode());
        assertTrue(response.containsHeader("Location"));
        String inventoryLocation = response.getFirstHeader("Location").getValue();

        //READ

        HttpGet httpGet = new HttpGet(new URI(inventoryLocation));
        HttpResponse readResponse = httpClient.execute(httpGet);

        assertEquals(200, readResponse.getStatusLine().getStatusCode());

        JSONObject getResponseObject = new JSONObject(readResponse.getEntity().getContent());
        assertEquals(inventoryLocation, getResponseObject.getLong("id"));
        assertEquals("com.tailoredshapes.test", getResponseObject.getString("category"));
        assertEquals(0, getResponseObject.getJSONArray("metrics").length());

        //Update
        parameters = new ArrayList<>();
        HttpPost updatePost = new HttpPost(new URI(inventoryLocation));

        JSONObject updatedObject = new JSONObject(getResponseObject.toString());
        JSONArray metrics = updatedObject.getJSONArray("metrics");

        JSONObject metricJson = new JSONObject();
        metricJson.put("value", "Cassie");
        metricJson.put("type", "string");

        metrics.put(metricJson);

        parameters.add(new BasicNameValuePair("inventory", updatedObject.toString()));

        updatePost.setEntity(new UrlEncodedFormEntity(parameters));

        HttpResponse updateResponse = httpClient.execute(updatePost);
        assertEquals(302, readResponse.getStatusLine().getStatusCode());
        assertTrue(updateResponse.containsHeader("Location"));
        String updateLocation = updateResponse.getFirstHeader("Location").getValue();
        assertNotSame(inventoryLocation, updateLocation);


        //READ

        HttpGet updateGet = new HttpGet(new URI(updateLocation));
        HttpResponse updatedResponse = httpClient.execute(updateGet);

        assertEquals(200, updatedResponse.getStatusLine().getStatusCode());

        JSONObject updatedResponseObject = new JSONObject(updatedResponse.getEntity().getContent());
        assertEquals(inventoryLocation, updatedResponseObject.getLong("id"));
        assertEquals("com.tailoredshapes.test", updatedResponseObject.getString("category"));
        assertEquals(1, updatedResponseObject.getJSONArray("metrics").length());
    }
}

