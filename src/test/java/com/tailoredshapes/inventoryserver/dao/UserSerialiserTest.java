package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.serialisers.UserSerialiser;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Base64;
import java.util.HashSet;

import static org.junit.Assert.*;

public class UserSerialiserTest {
    @Test
    public void testShouldSerializeAUser() throws Exception {
        Inventory inventory = new InventoryBuilder().build();
        HashSet<Inventory> inventories = new HashSet<>();
        inventories.add(inventory);
        User user = new UserBuilder().inventories(inventories).build();

        UserSerialiser userSerialiser = GuiceTest.injector.getInstance(UserSerialiser.class);

        JSONObject jsonObject = new JSONObject(new String(userSerialiser.serialise(user)));


        assertEquals(user.getId(), (Long) jsonObject.getLong("id"));
        assertEquals(user.getName(), jsonObject.getString("name"));
        assertArrayEquals(user.getPublicKey().getEncoded(), Base64.getDecoder().decode(jsonObject.getString("publicKey")));
        assertFalse(jsonObject.has("privateKey"));
        assertTrue(jsonObject.has("inventories"));
        assertEquals(1, jsonObject.getJSONArray("inventories").length());
    }
}
