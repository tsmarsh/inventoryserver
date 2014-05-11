package com.tailoredshapes.inventoryserver.model;

import com.impetus.kundera.index.IndexCollection;
import com.impetus.kundera.index.Index;

import javax.persistence.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Cacheable
@Table(name = "users")
@IndexCollection(columns = {@Index(name = "name")})
public class User implements Idable<User>, Keyed, Cloneable, ShallowCopy<User> {

    @Id
    @Column(name = "user_id")
    private Long id;

    @Column(updatable = false, name = "user_name", nullable = false)
    private String name;

    @Column(length = 1024, updatable = false, name = "private_key", nullable = false)
    private PrivateKey privateKey;

    @Column(length = 1024, updatable = false, name = "public_key", nullable = false)
    private PublicKey publicKey;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_inventories",
               joinColumns = @JoinColumn(name = "user_id", updatable = false),
               inverseJoinColumns = @JoinColumn(name="inventory_id", updatable = false))
    private Collection<Inventory> inventories = new HashSet<>();

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
