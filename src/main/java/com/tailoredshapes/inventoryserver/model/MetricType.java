package com.tailoredshapes.inventoryserver.model;

public class MetricType implements Idable<MetricType>{
    Long id;
    String name;

    public Long getId() {
        return id;
    }

    public MetricType setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public MetricType setName(String name) {
        this.name = name;
        return this;
    }
}
