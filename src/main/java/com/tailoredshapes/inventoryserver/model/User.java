package com.tailoredshapes.inventoryserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Entity
public class User implements Idable<User>, Keyed, Cloneable, ShallowCopy<User> {

    @Id
    private Long id;

    @Column
    private String name;

    @Column(length = 1024)
    private PrivateKey privateKey;

    @Column(length = 1024)
    private PublicKey publicKey;

    @ManyToMany
    private Collection<Inventory> inventories = new HashSet<Inventory>();

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

    public User setInventories(Collection<Inventory> inventories) {
        this.inventories = inventories;
        return this;
    }

    @Override
    public User clone() throws CloneNotSupportedException {
        return (User) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        return !(name != null ? !name.equals(user.name) : user.name != null) && !(privateKey != null ? !Arrays.equals(privateKey.getEncoded(), user.privateKey.getEncoded()) : user.privateKey != null) && !(publicKey != null ? !Arrays.equals(publicKey.getEncoded(), user.publicKey.getEncoded()) : user.publicKey != null);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (privateKey != null ? Arrays.hashCode(privateKey.getEncoded()) : 0);
        result = 31 * result + (publicKey != null ? Arrays.hashCode(publicKey.getEncoded()) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", privateKey=" + privateKey +
                ", publicKey=" + publicKey +
                ", inventories=" + inventories +
                '}';
    }

    @Override
    public User shallowCopy() {
        return new User().setId(null)
                .setPrivateKey(privateKey)
                .setPublicKey(publicKey)
                .setInventories(inventories);
    }
}
