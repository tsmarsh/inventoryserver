package com.tailoredshapes.inventoryserver.model;

/**
 * Created by tmarsh on 2/14/14.
 */
public class Category implements Idable<Category>{
    Long id;
    String name;
    String fullname;

    public Category(){};

    public Category getParent() {
        return parent;
    }

    public Category setParent(Category parent) {
        this.parent = parent;
        return this;
    }

    Category parent;

    public Long getId() {
        return id;
    }

    public Category setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getFullname() {
        return fullname;
    }

    public Category setFullname(String fullname) {
        this.fullname = fullname;
        return this;
    }
}
