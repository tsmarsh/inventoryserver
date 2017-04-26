package com.tailoredshapes.inventoryserver.repositories;

import java.util.Collection;
import java.util.function.Predicate;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;

public interface Repository {

  @FunctionalInterface
  interface FindById<T> {
    T findById(Long extract);
  }

  @FunctionalInterface
  interface FindBy<T, Z> {
    T findBy(Finder<T, Z> finder);
  }

  @FunctionalInterface
  interface ListBy<T, Z> {
    Collection<T> listby(Finder<Collection<T>, Z> finder);
  }

  @FunctionalInterface
  interface List<T> {
    Collection<T> list();
  }

  @FunctionalInterface
  interface Save<T> {
    T save(T t);
  }

  static <T extends Idable> Save<T> save(DAO<T> dao) {
    return (t) -> t.getId() == null ? dao.create(t) : dao.update(t);
  }

  static <T extends Idable> Save<T> saveNested(T parent, DAO<T> dao, Predicate<T> filter, List<T> lister, Save<T> saver ) {
    return (t) -> {
      T savedT = t.getId() == null ? dao.create(t) : dao.update(t);
      Collection<T> ts = lister.list();
      ts.removeIf(filter);
      ts.add(savedT);
      saver.save(parent);
      return savedT;
    };
  }

  static List<Inventory> listFromUser(User parent) {
    return parent::getInventories;
  }
}
