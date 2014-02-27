package com.tailoredshapes.inventoryserver.dao;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserSerialiserTest {
    @Test
    public void testShouldSerializeAUser() throws Exception {
        User user = new UserBuilder().build();

        UserSerialiser userSerialiser = new UserSerialiser();
        JSONObject jsonObject = new JSONObject(new String(userSerialiser.serialise(user)));
        assertEquals(user.getId(), (Long) jsonObject.getLong("id"));
        assertEquals(user.getName(), jsonObject.getString("name"));
        assertArrayEquals(user.getPublicKey().getEncoded(), Base64.decode(jsonObject.getString("publicKey")));
        assertFalse(jsonObject.has("privateKey"));
    }
}
