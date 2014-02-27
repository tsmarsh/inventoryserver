package com.tailoredshapes.inventoryserver.utils;

import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryUserRepository;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UserParserTest {
    User existingUser;
    Serialiser<User> serializer;
    Encoder encoder;
    InMemoryUserDAO dao;
    User savedUser;
    UserRepository repo;
    private KeyProvider keyprovider;

    @Before
    public void setUp() throws Exception {
        existingUser = new UserBuilder().id(555l).name("Cassie").build();
        serializer = new JSONSerialiser<>();
        encoder = new RSAEncoder();
        keyprovider = new RSAKeyProvider();

        dao = new InMemoryUserDAO(serializer, encoder, keyprovider);
        savedUser = dao.create(existingUser);
        repo = new InMemoryUserRepository(dao);
    }

    @Test
    public void testParseNewUser() throws Exception {
        JSONObject userJSON = new JSONObject().put("name", "Archer");

        UserParser userParser = new UserParser(repo);
        User parsedUser = userParser.parse(userJSON.toString());
        assertEquals("Archer", parsedUser.getName());
    }

    @Test
    public void testParseExistingUser() throws Exception {
        savedUser.setName("Archer");
        String userJsonString = new String(serializer.serialise(savedUser));

        UserParser userParser = new UserParser(repo);
        User parsedUser = userParser.parse(userJsonString);

        assertEquals("Archer", parsedUser.getName());
        assertArrayEquals(savedUser.getPrivateKey().getEncoded(), parsedUser.getPrivateKey().getEncoded());
        assertArrayEquals(savedUser.getPublicKey().getEncoded(), parsedUser.getPublicKey().getEncoded());
    }
}
