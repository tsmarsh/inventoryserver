package com.tailoredshapes.inventoryserver.model;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collection;
import java.util.Map;

public class User implements Idable<User>, Keyed, Cloneable {
    private Long id;
    private String name;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Collection<Inventory> inventories;

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

    public Collection<Inventory> getInventories() {
        return this.inventories;
    }

    public User setInventories(Collection inventories) {
        this.inventories = inventories;
        return this;
    }

    @Override
    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }
}
