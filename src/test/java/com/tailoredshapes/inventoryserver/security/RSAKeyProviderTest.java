package com.tailoredshapes.inventoryserver.security;

import org.junit.Test;

import java.security.KeyPair;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RSAKeyProviderTest {
    @Test
    public void testGenerate() throws Exception {
        RSAKeyProvider rsaKeyProvider = new RSAKeyProvider();
        KeyPair generate = rsaKeyProvider.generate();
        assertNotNull(generate);
        assertEquals("RSA", generate.getPrivate().getAlgorithm());
        assertEquals("RSA", generate.getPublic().getAlgorithm());
    }
}
