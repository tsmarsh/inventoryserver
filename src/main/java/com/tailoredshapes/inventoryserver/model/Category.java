package com.tailoredshapes.inventoryserver.model;

public class Category implements Idable<Category>, Cloneable {
    private Long id;
    private String name;
    private String fullname;
    private Category parent;

    public Category() {
    }

    public Category getParent() {
        return parent;
    }

    public Category setParent(Category parent) {
        this.parent = parent;
        return this;
    }

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

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", fullname='" + fullname + '\'' +
                ", parent=" + parent +
                '}';
    }

    @Override
    protected Category clone() throws CloneNotSupportedException {
       return (Category) super.clone();
    }
}
