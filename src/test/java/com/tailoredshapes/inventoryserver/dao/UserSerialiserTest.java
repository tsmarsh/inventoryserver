package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import com.tailoredshapes.inventoryserver.serialisers.UserSerialiser;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.HashSet;

import static org.junit.Assert.*;

public class UserSerialiserTest {


    private User user;
    private SimpleScope scope;

    @Before
    public void setUp() throws Exception {
        Inventory inventory = new InventoryBuilder().build();
        HashSet<Inventory> inventories = new HashSet<>();
        inventories.add(inventory);
        user = new UserBuilder().inventories(inventories).build();
        scope = GuiceTest.injector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), user);
    }

    @After
    public void tearDown() throws Exception {
        scope.exit();
    }
    @Test
    public void testShouldSerializeAUser() throws Exception {
        UserSerialiser userSerialiser = GuiceTest.injector.getInstance(UserSerialiser.class);
        UrlBuilder<User> urlBuilder = GuiceTest.injector.getInstance(new Key<UrlBuilder<User>>(){});
        JSONObject jsonObject = new JSONObject(new String(userSerialiser.serialise(user)));


        assertEquals(urlBuilder.build(user), jsonObject.getString("id"));
        assertEquals(user.getName(), jsonObject.getString("name"));
        assertArrayEquals(user.getPublicKey().getEncoded(), Base64.getDecoder().decode(jsonObject.getString("publicKey")));
        assertFalse(jsonObject.has("privateKey"));
        assertTrue(jsonObject.has("inventories"));
        assertEquals(1, jsonObject.getJSONArray("inventories").length());
    }
}
