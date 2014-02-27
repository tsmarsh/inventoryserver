package com.tailoredshapes.inventoryserver.utils;

import com.google.inject.Inject;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class RSAKeyProvider implements KeyProvider<RSA> {

    private final int keysize;
    private final String RSA = "RSA";

    @Inject
    public RSAKeyProvider() {
        this.keysize = 512;
    }

    @Override
    public KeyPair generate() {
        KeyPairGenerator keyGen = null;
        try {
            keyGen = KeyPairGenerator.getInstance(RSA);
            keyGen.initialize(keysize);
            return keyGen.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
