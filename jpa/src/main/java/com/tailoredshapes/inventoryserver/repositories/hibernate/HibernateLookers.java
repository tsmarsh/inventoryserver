package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Looker;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

public interface HibernateLookers {

  Looker<String, Category, EntityManager> catergoryByFullName = (categoryFullName) ->
    (manager) -> {
      Query query = manager.createQuery("select c from Category c where c.fullname = :fullname")
        .setParameter("fullname", categoryFullName);

      Category cat;
      try {
        cat = manager.merge((Category) query.getSingleResult());
      } catch (Exception e) {
        cat = new Category().setFullname(categoryFullName);
      }
      return cat;
    };

  Looker<String, Inventory, EntityManager> inventoryById = (id) -> (db) -> db.find(Inventory.class, id);

  Looker<String, MetricType, EntityManager> metricTypeByName = (name) -> (manager) -> {
    Query cq = manager.createQuery("select m from MetricType m where m.name = :name")
      .setParameter("name", name);

    MetricType type;
    try {
      type = manager.merge((MetricType) cq.getSingleResult());
    } catch (Exception e) {
      type = new MetricType().setName(name);
    }
    return type;
  };

  Looker<String, User, EntityManager> userByName = (name) ->
    (EntityManager manager) -> {
      Query cq = manager.createQuery("select u from User u where u.name = :name order by u.created desc ")
        .setParameter("name", name).setMaxResults(1);

      User type;
      try {
        User user = (User) cq.getSingleResult();
        type = manager.merge(user);
      } catch (Exception e) {
        type = new User().setName(name);
      }
      return type;
    };

  Looker<String, User, EntityManager> userById = (id) -> (db) -> db.find(User.class, id);
}
