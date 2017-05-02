package com.tailoredshapes.inventoryserver.dao;

public class ChildFreeSaver<T> implements Saver<T> {

  @Override
  public T saveChildren(DAO<T> dao, T object) {
    return object;
  }
}

