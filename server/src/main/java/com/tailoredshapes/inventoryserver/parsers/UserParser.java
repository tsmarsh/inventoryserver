package com.tailoredshapes.inventoryserver.parsers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import org.json.JSONArray;
import org.json.JSONObject;

public interface UserParser {

  static Parser<User> userParser(Repository.FindById<User> findById,
                                 Parser<Inventory> inventoryParser,
                                 IdExtractor<Long> idExtractor) {
    return (s) -> {
      JSONObject jsonUser = new JSONObject(s);
      User user = new User();

      if (jsonUser.has("id")) {
        String id = jsonUser.getString("id");
        try {
          user = findById.findById(idExtractor.extract(new URL(id).getPath()));
        } catch (MalformedURLException e) {
          e.printStackTrace();
        }
      }

      String name = jsonUser.getString("name");
      user.setName(name);

      Set<Inventory> inventorySet = new HashSet<>();

      if (jsonUser.has("inventories")) {
        JSONArray inventories = jsonUser.getJSONArray("inventories");
        for (int i = 0; i < inventories.length(); i++) {
          inventorySet.add(inventoryParser.parse(inventories.getJSONObject(i).toString()));
        }
      }

      user.setInventories(inventorySet);

      return user;
    };
  }
}
