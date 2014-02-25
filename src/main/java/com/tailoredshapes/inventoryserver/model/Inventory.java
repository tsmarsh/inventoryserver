package com.tailoredshapes.inventoryserver.model;

import java.util.List;

public class Inventory implements Idable<Inventory> {
    private Long id;
    private User user;
    private Category category;
    private List<Metric> metrics;
    private Inventory parent;

    public Long getId() {
        return id;
    }

    public Inventory setId(Long id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Inventory setUser(User user) {
        this.user = user;
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
}
