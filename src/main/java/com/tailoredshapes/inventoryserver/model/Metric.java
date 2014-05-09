package com.tailoredshapes.inventoryserver.model;

import javax.persistence.*;

@Entity
@Cacheable
@Table
public class Metric implements Idable<Metric>, Cloneable, ShallowCopy<Metric> {

    @Id
    @Column(name = "metric_id")
    private Long id;

    @Column(nullable = false, updatable = false, name = "metric_value")
    private String value;

    @ManyToOne(optional = false)
    @JoinColumn(name = "metric_type_id")
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

        return !(id != null ? !id.equals(metric.id) : metric.id != null) && type.equals(metric.type) && value.equals(metric.value);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + value.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public Metric shallowCopy() {
        return new Metric().setId(null).setType(type).setValue(value);
    }
}
