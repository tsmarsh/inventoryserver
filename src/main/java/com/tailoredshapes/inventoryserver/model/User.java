package com.tailoredshapes.inventoryserver.model;

/**
 * Created by tmarsh on 2/14/14.
 */
public class User implements Idable<User>{
    Long id;
    String name;
    String privateKey;
    String publicKey;

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

    public String getPrivateKey() {
        return privateKey;
    }

    public User setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public User setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }
}
