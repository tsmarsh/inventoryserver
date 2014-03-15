package com.tailoredshapes.inventoryserver.model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public class User implements Idable<User>, Keyed {
    private Long id;
    private String name;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Map<Long, Inventory> inventories;

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public User setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public User setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public Map<Long, Inventory> getInventories() {
        return this.inventories;
    }

    public User setInventories(Map<Long, Inventory> inventories) {
        this.inventories = inventories;
        return this;
    }
}
