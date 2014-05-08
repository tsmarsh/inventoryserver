package com.tailoredshapes.inventoryserver.servlets;

import org.apache.http.*;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class PestletTest {

    @Test
    public void testCanHandleUserRootRequestsInHibernate() throws Exception {
        int port = 7777;

        final Server server = new Server(port);
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");

        webAppContext.setWar(this.getClass().getResource("/hibernate").getPath());
        server.setHandler(webAppContext);
        server.start();

        try {
            testCanCreateAUser(port);
        } finally {
            server.stop();
        }
    }

    @Test
    public void testCanHandleUserRootRequestsInMemory() throws Exception {
        int port = 6666;

        final Server server = new Server(port);
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");

        webAppContext.setWar(this.getClass().getResource("/memory").getPath());
        server.setHandler(webAppContext);
        server.start();

        try {
            testCanCreateAUser(port);
        } finally {
            server.stop();
        }
    }


    public void testCanCreateAUser(Integer port) throws Exception {
        CloseableHttpClient httpClient = HttpClients.custom().setRedirectStrategy(new RedirectStrategy() {
            @Override
            public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                return false;
            }

            @Override
            public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context) throws ProtocolException {
                return null;
            }
        }).build();

        // User Root

        String userUrl = createUser(port, httpClient);

        readSavedUserHead(httpClient, port, userUrl);

        readSavedUser(httpClient, userUrl);

        listSavedUser(httpClient, port);

        String inventoryLocation = createInventoryForUser(httpClient, userUrl);

        JSONObject getResponseObject = readSavedInventory(httpClient, inventoryLocation, "com.tailoredshapes.test");

        String updateLocation = updateInventory(httpClient, inventoryLocation, getResponseObject);

        readUpdatedInventory(httpClient, updateLocation, "com.tailoredshapes.test");

        // Inventory Root
        String inventoryRootLocation = createInventory(port, httpClient);

        JSONObject savedInventory = readSavedInventory(httpClient, inventoryRootLocation, "com.test.inventory");

        String updatedInventoryLocation = updateInventory(httpClient, inventoryRootLocation, savedInventory);

        readUpdatedInventory(httpClient, updatedInventoryLocation, "com.test.inventory");
    }

    private String createInventory(Integer port, CloseableHttpClient httpClient) throws URISyntaxException, IOException {

        JSONObject inventoryJsonObject = new JSONObject();
        inventoryJsonObject.put("category", "com.test.inventory");

        HttpPost inventoryPost = new HttpPost(new URI(String.format("http://localhost:%d/inventories", port)));
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("inventory", inventoryJsonObject.toString()));
        inventoryPost.setEntity(new UrlEncodedFormEntity(parameters));
        HttpResponse inventoryResponse = httpClient.execute(inventoryPost);

        Header location = inventoryResponse.getFirstHeader("Location");

        inventoryPost.releaseConnection();
        return location.getValue();
    }

    private String createUser(Integer port, CloseableHttpClient httpClient) throws URISyntaxException, IOException {//CREATE USER

        JSONObject userJsonObject = new JSONObject();
        userJsonObject.put("name", "Archer");

        HttpPost userPost = new HttpPost(new URI(String.format("http://localhost:%d/users", port)));
        List<NameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("user", userJsonObject.toString()));
        userPost.setEntity(new UrlEncodedFormEntity(parameters));
        HttpResponse userReponse = httpClient.execute(userPost);
        Header location = userReponse.getFirstHeader("Location");
        String userUrl = location.getValue();

        userPost.releaseConnection();
        return userUrl;
    }

    private void readSavedUserHead(CloseableHttpClient httpClient, int port, String userUrl) throws URISyntaxException, IOException {
        HttpGet userHeadGet = new HttpGet(new URI(String.format("http://localhost:%d/users/Archer", port)));
        HttpResponse response = httpClient.execute(userHeadGet);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(302);

        String location = response.getFirstHeader("Location").getValue();

        userHeadGet.releaseConnection();

        assertThat(location).isEqualTo(userUrl);
    }

    private void readSavedUser(CloseableHttpClient httpClient, String userUrl) throws IOException {
        HttpGet userGet = new HttpGet(userUrl);
        HttpResponse response = httpClient.execute(userGet);
        String userResponseString = EntityUtils.toString(response.getEntity());
        userGet.releaseConnection();

        JSONObject readUser = new JSONObject(userResponseString);
        assertThat(readUser.getString("name")).isEqualTo("Archer");
        assertThat(readUser.getString("publicKey")).isNotNull();
    }

    private void listSavedUser(CloseableHttpClient httpClient, int port) throws IOException {//READ USER
        HttpGet userListGet = new HttpGet(String.format("http://localhost:%d/users", port));
        HttpResponse response = httpClient.execute(userListGet);
        String userListResponse = EntityUtils.toString(response.getEntity());
        userListGet.releaseConnection();

        JSONObject userList = new JSONObject(userListResponse);
        JSONArray users = userList.getJSONArray("users");
        assertThat(users.length()).isEqualTo(1);
        JSONObject archer = users.getJSONObject(0);
        assertThat(archer.getString("name")).isEqualTo("Archer");
        assertThat(archer.getString("publicKey")).isNotNull();
    }

    private String createInventoryForUser(CloseableHttpClient httpClient, String userUrl) throws URISyntaxException, IOException {
        List<NameValuePair> parameters;//CREATE

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("category", "com.tailoredshapes.test");
        jsonObject.put("user", userUrl);

        HttpPost httpPost = new HttpPost(new URI(String.format("%s/inventories", userUrl)));
        parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("inventory", jsonObject.toString()));
        httpPost.setEntity(new UrlEncodedFormEntity(parameters));


        HttpResponse createInventoryResponse = httpClient.execute(httpPost);
        assertThat(createInventoryResponse.getStatusLine().getStatusCode()).isEqualTo(302);
        String inventoryLocation = createInventoryResponse.getFirstHeader("Location").getValue();

        httpPost.releaseConnection();
        return inventoryLocation;
    }

    private JSONObject readSavedInventory(CloseableHttpClient httpClient, String inventoryLocation, String category) throws IOException, URISyntaxException {
        URI uri = new URI(inventoryLocation);
        HttpGet httpGet = new HttpGet(uri);

        HttpResponse readResponse = httpClient.execute(httpGet);
        assertThat(readResponse.getStatusLine().getStatusCode()).isEqualTo(200);

        String inventoryJsonString = EntityUtils.toString(readResponse.getEntity());

        httpGet.releaseConnection();

        JSONObject getResponseObject = new JSONObject(inventoryJsonString);
        assertThat(getResponseObject.getString("id")).isEqualTo(inventoryLocation);
        assertThat(getResponseObject.getString("category")).isEqualTo(category);
        assertThat(getResponseObject.getJSONArray("metrics").length()).isEqualTo(0);
        return getResponseObject;
    }

    private String updateInventory(CloseableHttpClient httpClient, String inventoryLocation, JSONObject getResponseObject) throws IOException, URISyntaxException {
        List<NameValuePair> parameters;
        URI uri = new URI(inventoryLocation);
        parameters = new ArrayList<>();
        HttpPost updatePost = new HttpPost(uri);

        JSONObject updatedObject = new JSONObject(getResponseObject.toString());
        JSONArray metrics = updatedObject.getJSONArray("metrics");

        JSONObject metricJson = new JSONObject();
        metricJson.put("value", "Cassie");
        metricJson.put("type", "string");

        metrics.put(metricJson);

        parameters.add(new BasicNameValuePair("inventory", updatedObject.toString()));

        updatePost.setEntity(new UrlEncodedFormEntity(parameters));

        HttpResponse updateResponse = httpClient.execute(updatePost);
        assertThat(updateResponse.getStatusLine().getStatusCode()).isEqualTo(302);
        String updateLocation = updateResponse.getFirstHeader("Location").getValue();
        assertThat(updateLocation).isNotEqualTo(inventoryLocation);

        updatePost.releaseConnection();
        return updateLocation;
    }

    private void readUpdatedInventory(CloseableHttpClient httpClient, String updateLocation, String category) throws URISyntaxException, IOException {//READ

        HttpGet updateGet = new HttpGet(new URI(updateLocation));
        HttpResponse updatedResponse = httpClient.execute(updateGet);

        assertThat(updatedResponse.getStatusLine().getStatusCode()).isEqualTo(200);

        String updatedInventoryString = EntityUtils.toString(updatedResponse.getEntity());

        updateGet.releaseConnection();

        JSONObject updatedResponseObject = new JSONObject(updatedInventoryString);
        assertThat(updatedResponseObject.getString("id")).isEqualTo(updateLocation);
        assertThat(updatedResponseObject.getString("category")).isEqualTo(category);
        assertThat(updatedResponseObject.getJSONArray("metrics").length()).isEqualTo(1);
    }
}

