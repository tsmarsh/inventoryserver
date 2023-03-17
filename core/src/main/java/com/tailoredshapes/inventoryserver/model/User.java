package com.tailoredshapes.inventoryserver.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Cacheable
@Table(name = "users")
public class User implements Idable<User>, Cloneable, ShallowCopy<User> {

  @Id
  @Column(name = "user_id")
  private Long id;

  @Column(updatable = false, name = "user_name", nullable = false)
  private String name;

  @Column(name = "created", updatable = false, nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date created;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_inventories",
    joinColumns = @JoinColumn(name = "user_id", updatable = false),
    inverseJoinColumns = @JoinColumn(name = "inventory_id", updatable = false))
  private Collection<Inventory> inventories = new HashSet<>();

  public User() {
    this.created = new Date();
  }

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

  public Date getCreated() {
    return created;
  }

  public User setCreated(Date created) {
    this.created = created;
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
    if (this == o) { return true; }
    if (!(o instanceof User)) { return false; }

    User user = (User) o;

    return id.equals(user.id) && !(name != null ? !name.equals(user.name) : user.name != null);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "User{" +
           "id=" + id +
           ", name='" + name + '\'' +
           ", inventories=" + inventories +
           '}';
  }

  @Override
  public User shallowCopy() {
    return new User().setId(null).setName(name)
      .setInventories(inventories);
  }
}
