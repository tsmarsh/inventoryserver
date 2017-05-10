package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;

public class MetricSaver implements Saver<Metric> {


  private final DAO<MetricType> dao;

  public MetricSaver(DAO<MetricType> dao) {
    this.dao = dao;
  }

  @Override
  public Metric saveChildren(DAO<Metric> mdao, Metric object) {
    object.setType(dao.upsert(object.getType()));
    return object;
  }
}
