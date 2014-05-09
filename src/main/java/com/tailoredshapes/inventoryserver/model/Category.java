package com.tailoredshapes.inventoryserver.model;

import javax.persistence.*;

@Entity
@Cacheable
@Table(name = "categories")
public class Category implements Idable<Category>, Cloneable, ShallowCopy<Category> {
    @Id
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, updatable = false, name = "category_name")
    private String name;

    @Column(nullable = false, updatable = false, name = "fullname")
    private String fullname;

    @ManyToOne
    @JoinColumn(name = "category_parent_id")
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
    public Category clone() throws CloneNotSupportedException {
        return (Category) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;

        Category category = (Category) o;

        return fullname.equals(category.fullname) && !(id != null ? !id.equals(category.id) : category.id != null) && !(name != null ? !name.equals(category.name) : category.name != null) && !(parent != null ? !parent.equals(category.parent) : category.parent != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + fullname.hashCode();
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public Category shallowCopy() {
        return new Category()
                .setId(null)
                .setName(name)
                .setFullname(fullname)
                .setParent(parent);
    }
}
