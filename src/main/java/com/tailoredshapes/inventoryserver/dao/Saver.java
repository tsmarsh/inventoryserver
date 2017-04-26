package com.tailoredshapes.inventoryserver.dao;

public interface Saver<T> {
  T saveChildren(DAO<T> dao, T object);
}
