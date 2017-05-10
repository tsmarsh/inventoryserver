package com.tailoredshapes.inventoryserver.builders;

import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;

public class MetricBuilder {
  private Long id = null;
  private String value = "value";
  private MetricType type;

  public MetricBuilder() {
    type = new MetricTypeBuilder().build();
  }

  public MetricBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public MetricBuilder value(String value) {
    this.value = value;
    return this;
  }

  public MetricBuilder type(MetricType type) {
    this.type = type;
    return this;
  }

  public Metric build() {
    return new Metric()
      .setId(id)
      .setValue(value)
      .setType(type);
  }
}
