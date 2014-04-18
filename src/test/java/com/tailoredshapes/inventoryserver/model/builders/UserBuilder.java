package com.tailoredshapes.inventoryserver.model.builders;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

import java.security.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class UserBuilder {

    private final User user;
    Long id = 555l;
    String name = "Archer";
    Collection<Inventory> inventoryMap = new HashSet<Inventory>();
    PrivateKey privateKey;
    PublicKey publicKey;

    public UserBuilder() {
        user = new User();
        KeyPairGenerator rsa1024;
        try {
            rsa1024 = KeyPairGenerator.getInstance("RSA");
            rsa1024.initialize(1024);
            KeyPair keyPair = rsa1024.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public UserBuilder id(Long id) {
        this.id = id;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder privateKey(PrivateKey key) {
        this.privateKey = key;
        return this;
    }

    public UserBuilder publicKey(PublicKey key) {
        this.publicKey = key;
        return this;
    }

    public UserBuilder inventories(Collection inventoryMap) {
        this.inventoryMap = inventoryMap;
        return this;
    }

    public User build() {
        return user.setId(id).setName(name).setPrivateKey(privateKey).setPublicKey(publicKey).setInventories(inventoryMap);
    }
}
