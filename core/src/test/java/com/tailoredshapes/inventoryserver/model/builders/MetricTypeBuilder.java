package com.tailoredshapes.inventoryserver.model.builders;

import com.tailoredshapes.inventoryserver.model.MetricType;

public class MetricTypeBuilder {
  private Long id = null;
  private String name = "test";

  public MetricTypeBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public MetricTypeBuilder name(String name) {
    this.name = name;
    return this;
  }

  public MetricType build() {
    return new MetricType().setId(id).setName(name);
  }
}
