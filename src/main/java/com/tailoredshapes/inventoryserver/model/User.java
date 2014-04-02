package com.tailoredshapes.inventoryserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collection;
import java.util.Collections;

@Entity
public class User implements Idable<User>, Keyed, Cloneable {

    @Id
    private Long id;

    @Column
    private String name;

    @Column(length = 1024)
    private PrivateKey privateKey;

    @Column(length = 1024)
    private PublicKey publicKey;

    @ManyToMany
    private Collection<Inventory> inventories = Collections.emptySet();

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
