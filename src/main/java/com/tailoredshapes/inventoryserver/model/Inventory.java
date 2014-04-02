package com.tailoredshapes.inventoryserver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Inventory implements Idable<Inventory>, Cloneable {

    @Id
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,
               optional = false)
    private Category category;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Metric> metrics = new ArrayList<>();

    @ManyToOne(cascade = CascadeType.ALL)
    private Inventory parent;

    public Long getId() {
        return id;
    }

    public Inventory setId(Long id) {
        this.id = id;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Inventory setCategory(Category category) {
        this.category = category;
        return this;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public Inventory setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
        return this;
    }

    public Inventory getParent() {
        return parent;
    }

    public Inventory setParent(Inventory parent) {
        this.parent = parent;
        return this;
    }

    @Override
    public Inventory clone() throws CloneNotSupportedException {
        return (Inventory) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;

        Inventory inventory = (Inventory) o;

        if (!category.equals(inventory.category)) return false;
        if (id != null ? !id.equals(inventory.id) : inventory.id != null) return false;

        for (Metric metric : metrics) {
            if (!inventory.getMetrics().contains(metric)) {
                return false;
            }
        }

        return !(parent != null ? !parent.equals(inventory.parent) : inventory.parent != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + category.hashCode();
        for (Metric metric : metrics) {
            result = 31 * result + metric.hashCode();
        }
        result = 31 * result + (parent != null ? parent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", category=" + category +
                ", metrics=" + metrics +
                ", parent=" + parent +
                '}';
    }
}
