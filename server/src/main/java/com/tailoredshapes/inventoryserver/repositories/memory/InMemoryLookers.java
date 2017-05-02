package com.tailoredshapes.inventoryserver.repositories.memory;

import java.util.Map;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Looker;

import static com.tailoredshapes.underbar.UnderBar.filter;
import static com.tailoredshapes.underbar.UnderBar.first;

public interface InMemoryLookers {
  Looker<String, Category, Map<Long, Category>> categoryByFullName = (categoryFullName) -> (db) -> {
    for (Category cat : db.values()) {
      if (cat.getFullname().equals(categoryFullName)) {
        return cat;
      }
    }

    return new Category().setFullname(categoryFullName);
  };

  Looker<Long, Inventory, Map<Long, Inventory>> inventoryById = id -> db -> db.get(id);

  Looker<String, MetricType, Map<Long, MetricType>> metricTypeByName = name -> db -> {
    for (MetricType type : db.values()) {
      if (type.getName().equals(name)) {
        return type;
      }
    }

    return new MetricType().setName(name);
  };

  Looker<String, User, Map<Long, User>> userByName =
    name -> db -> first(filter(db.values(), (u) -> u.getName().equals(name)));

  Looker<Long, User, Map<Long, User>> userById = id -> db -> db.get(id);
}
