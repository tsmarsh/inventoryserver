package com.tailoredshapes.inventoryserver.model;

public class Metric implements Idable<Metric> {
    private Long id;
    private String value;
    private MetricType type;
    private Inventory inventory;

    public Long getId() {
        return id;
    }

    public Metric setId(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Metric setValue(String value) {
        this.value = value;
        return this;
    }

    public MetricType getType() {
        return type;
    }

    public Metric setType(MetricType type) {
        this.type = type;
        return this;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Metric setInventory(Inventory inventory) {
        this.inventory = inventory;
        return this;
    }
}
