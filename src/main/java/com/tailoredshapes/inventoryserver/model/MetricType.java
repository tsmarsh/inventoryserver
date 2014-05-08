package com.tailoredshapes.inventoryserver.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Cacheable
public class MetricType implements Idable<MetricType>, Cloneable, ShallowCopy<MetricType> {

    @Id
    private Long id;

    @Column(nullable = false, updatable = false)
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

    @Override
    protected MetricType clone() throws CloneNotSupportedException {
        return (MetricType) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetricType)) return false;

        MetricType that = (MetricType) o;

        return !(id != null ? !id.equals(that.id) : that.id != null) && name.equals(that.name);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        return result;
    }

    @Override
    public MetricType shallowCopy() {
        return new MetricType().setName(name).setId(null);
    }
}
