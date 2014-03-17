package com.tailoredshapes.inventoryserver.model;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class Inventory implements Idable<Inventory>, Cloneable {
    private Long id;
    private Category category;
    private List<Metric> metrics = new ArrayList<>();
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
    protected Inventory clone() throws CloneNotSupportedException {
        return (Inventory) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Inventory)) return false;

        Inventory inventory = (Inventory) o;

        if (!category.equals(inventory.category)) return false;
        if (id != null ? !id.equals(inventory.id) : inventory.id != null) return false;

        for(Metric metric : metrics){
            if(!inventory.getMetrics().contains(metric)){
                return false;
            }
        }

        if (parent != null ? !parent.equals(inventory.parent) : inventory.parent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + category.hashCode();
        for(Metric metric : metrics){
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
