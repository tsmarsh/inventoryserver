package com.tailoredshapes.inventoryserver.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Cacheable
@Table(name = "metric_types")
public class MetricType implements Idable<MetricType>, Cloneable, ShallowCopy<MetricType> {

  @Id
  @Column(name = "metric_type_id")
  private Long id;

  @Column(nullable = false, updatable = false, name = "metric_type_name")
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
    if (this == o) { return true; }
    if (!(o instanceof MetricType)) { return false; }

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
