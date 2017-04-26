package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.tailoredshapes.inventoryserver.repositories.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public interface HibernateRepository extends Repository {

  static <T> FindById<T> findById(Class<T> rawType, EntityManager manager) {
     return (id) -> manager.find(rawType, id);
  }

  static <T> FindBy<T, EntityManager> findBy(EntityManager manager) {
    return (finder) -> finder.find(manager);
  }

  static <T> ListBy<T, EntityManager> listBy(EntityManager manager){
    return (finder) -> finder.find(manager);
  }

  static <T> List<T> list(Class<T> rawType, EntityManager manager) {
    return () -> {
      String getAllTs = String.format("select t from %s t", rawType.getSimpleName());
      Query query = manager.createQuery(getAllTs);
      return query.getResultList();
    };
  }
}
