package com.tailoredshapes.inventoryserver.repositories.memory;

import java.util.List;
import java.util.Map;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Looker;

import static com.tailoredshapes.underbar.UnderBar.*;

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
    name -> db -> {
      List<User> filtered = filter(db.values(), (u) -> u.getName().equals(name));
      if (filtered.size() > 0) {
        return last(sortBy(filtered, User::getCreated));
      } else {
        return new User().setName(name);
      }
    };

  Looker<Long, User, Map<Long, User>> userById = id -> db -> db.get(id);
}
