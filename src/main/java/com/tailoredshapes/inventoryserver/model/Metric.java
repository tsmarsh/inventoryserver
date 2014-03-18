package com.tailoredshapes.inventoryserver.model;

public class Metric implements Idable<Metric>, Cloneable {
    private Long id;
    private String value;
    private MetricType type;

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

    @Override
    protected Metric clone() throws CloneNotSupportedException {
        return (Metric) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Metric)) return false;

        Metric metric = (Metric) o;

        if (id != null ? !id.equals(metric.id) : metric.id != null) return false;
        if (!type.equals(metric.type)) return false;
        return value.equals(metric.value);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + value.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
}
