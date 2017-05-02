package com.tailoredshapes.inventoryserver.model;

public interface Idable<T> {
  Long getId();

  T setId(Long id);
}
