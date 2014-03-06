package com.tailoredshapes.inventoryserver.model;

public class MetricType implements Idable<MetricType> {
    private Long id;
    private String name;

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

    @Override
    public String toString() {
        return "MetricType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
