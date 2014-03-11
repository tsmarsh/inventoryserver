package com.tailoredshapes.inventoryserver.security;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.InventoryModule;
import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.dao.ChildFreeSaver;
import com.tailoredshapes.inventoryserver.encoders.ByteArrayToLong;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.encoders.RSAEncoder;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryUserRepository;
import com.tailoredshapes.inventoryserver.serialisers.JSONSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class UserParserTest {
    Injector injector = Guice.createInjector(new InventoryModule("localhost", 5555));
    User existingUser;
    Serialiser<User> serializer;
    DAO<User> dao;
    User savedUser;
    UserRepository repo;

    @Before
    public void setUp() throws Exception {
        UserParser instance = injector.getInstance(UserParser.class);
        existingUser = new UserBuilder().id(555l).name("Cassie").build();
        serializer = new JSONSerialiser<>();

        dao = injector.getInstance(new Key<DAO<User>>(){});
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
