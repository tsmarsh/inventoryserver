package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.serialisers.UserSerialiser;
import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.Assert.*;

public class RSAEncoderTest {

    User user;
    Serialiser<User> serialiser;

    @Before
    public void setUp() throws Exception {
        KeyPairGenerator rsa1024 = KeyPairGenerator.getInstance("RSA");
        rsa1024.initialize(1024);
        KeyPair keyPair = rsa1024.generateKeyPair();
        user = new User().setId(1412l).setName("Archer").setPrivateKey(keyPair.getPrivate()).setPublicKey(keyPair.getPublic());
        serialiser = new UserSerialiser();
    }

    @Test
    public void testShouldNotReturnNull() throws Exception {
        RSAEncoder<User> rsaEncoder = new RSAEncoder<>(serialiser, new ByteArrayToLong());

        Long encode = rsaEncoder.encode(user);

        assertNotNull(encode);
    }

    @Test
    public void testShouldEncodeBitsConsistently() {
        RSAEncoder<User> rsaEncoder = new RSAEncoder<>(serialiser, new ByteArrayToLong());

        Long encode = rsaEncoder.encode(user);
        Long encode2 = rsaEncoder.encode(user);

        assertEquals(encode, encode2);
    }

    @Test
    public void testShouldProvideDifferentValuesForDifferentValues() throws Exception {
        RSAEncoder<User> rsaEncoder = new RSAEncoder<>(serialiser, new ByteArrayToLong());

        Long encode = rsaEncoder.encode(user);
        user.setName("Cassie");
        Long encode2 = rsaEncoder.encode(user);

        assertFalse(encode.equals(encode2));
    }
}
