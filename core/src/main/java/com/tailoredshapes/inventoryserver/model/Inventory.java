package com.tailoredshapes.inventoryserver.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Cacheable
@Table(name = "inventories")
public class Inventory implements Idable<Inventory>, Cloneable, ShallowCopy<Inventory> {

  @Id
  @Column(name = "inventory_id")
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.EAGER)
  @JoinColumn(name = "category_id")
  private Category category;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "inventory_metrics", joinColumns = @JoinColumn(name = "inventory_join_id", updatable = false), inverseJoinColumns = @JoinColumn(name = "metric_join_id", updatable = false))
  private List<Metric> metrics = new ArrayList<>();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "inventory_parent_id", updatable = false)
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
    if (this == o) { return true; }
    if (!(o instanceof Inventory)) { return false; }

    Inventory inventory = (Inventory) o;

    if (!category.equals(inventory.category)) { return false; }
    if (id != null ? !id.equals(inventory.id) : inventory.id != null) { return false; }

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

  @Override
  public Inventory shallowCopy() {
    return new Inventory().setId(null)
      .setCategory(category)
      .setMetrics(metrics)
      .setParent(parent);
  }
}
