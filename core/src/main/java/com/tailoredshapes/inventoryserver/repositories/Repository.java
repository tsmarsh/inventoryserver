package com.tailoredshapes.inventoryserver.repositories;

import java.util.Collection;
import java.util.function.Predicate;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

public interface Repository {

  static <T extends Idable> Save<T> save(DAO<T> dao) {
    return (t) -> t.getId() == null ? dao.create(t) : dao.update(t);
  }

  @FunctionalInterface
  interface FindById<T> {
    T findById(Long extract);
  }

  @FunctionalInterface
  interface FindBy<T, Z> {
    T findBy(Finder<T, Z> finder);
  }

  @FunctionalInterface
  interface List<T> {
    Collection<T> list();
  }

  @FunctionalInterface
  interface Save<T> {
    T save(T t);
  }
}
