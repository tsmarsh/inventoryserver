package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.tailoredshapes.inventoryserver.InventoryModule;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.serialisers.UserSerialiser;
import com.tailoredshapes.inventoryserver.utils.KeyProvider;
import com.tailoredshapes.inventoryserver.utils.RSA;
import com.tailoredshapes.inventoryserver.utils.RSAKeyProvider;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class InMemoryUserDAOTest {
    @Test
    public void testSaveChildren() throws Exception {
        Serialiser<User> serialiser = new UserSerialiser();
        Encoder<User, RSA> encoder = new RSAEncoder<>(serialiser, new ByteArrayToLong());
        KeyProvider<RSA> keyprovider = new RSAKeyProvider();
        InMemoryUserDAO<RSA> dao = new InMemoryUserDAO<>(encoder, keyprovider);
        User user = new User().setName("Archer");
        User savedUser = dao.create(user);
        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getPublicKey());
        assertNotNull(savedUser.getPrivateKey());
    }
}
