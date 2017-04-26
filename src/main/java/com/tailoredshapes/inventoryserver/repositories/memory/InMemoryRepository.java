package com.tailoredshapes.inventoryserver.repositories.memory;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Repository;

public interface InMemoryRepository<T extends Idable<T>> extends Repository {

  static <T> FindById<T> findById(Map<Long, T> db) {
    return db::get;
  }

  static <T> FindBy<T, Map<Long, T>> findBy(Map<Long, T> db) {
    return (finder) -> finder.find(db);
  }

  static <T> ListBy<T, Map<Long, T>> listBy(Map<Long, T> db) {
    return (finder) -> finder.find(db);
  }

  static <T> List<T> list(Map<Long, T> db) {
    return db::values;
  }
}
