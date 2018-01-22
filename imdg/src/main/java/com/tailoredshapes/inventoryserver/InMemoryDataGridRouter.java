package com.tailoredshapes.inventoryserver;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryLookers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static com.tailoredshapes.inventoryserver.InMemoryDataGridProviders.*;
import static com.tailoredshapes.underbar.UnderBar.*;
import static spark.Spark.get;
import static spark.Spark.post;


public interface InMemoryDataGridRouter {
  Logger log = LoggerFactory.getLogger(InMemoryDataGridRouter.class);

  static void route() {
    get("/inventories", "application/json", (req, res) -> {
      log.debug("allInventories");
      res.type("application/json");
      return inventoryList.list();
    }, (result) -> {
      JSONArray resp =
        new JSONArray(map((Collection<Inventory>) result, (i) -> new JSONObject(inventorySerialiser.serialise(i))));
      return resp.toString();
    });

    post("/inventories", "application/json", (req, res) -> {
      log.debug("createInventory");
      DAO<Inventory> dao = inventoryDAO;
      Parser<Inventory> parser = inventoryParser;
      Inventory inventory = parser.parse(req.body());
      Inventory saved = Repository.save(dao).save(inventory);

      res.redirect(inventoryUrlBuilder.build(saved), 303);
      return null;
    });

    get("/inventories/:id", "application/json", (req, res) -> {
          log.debug("readInventory");
          res.type("application/json");
          return inventoryById.findById(Long.parseLong(req.params("id")));
        }, (result) ->
          inventorySerialiser.serialise((Inventory) result)
    );

    get("/users", "application/json", (req, res) -> {
          log.debug("allUsers");
          res.type("application/json");
          return userList.list();
        },
        (result) -> {
          JSONArray resp =
            new JSONArray(map((Collection<User>) result, (i) -> new JSONObject(userSerialiser.serialise(i))));
          return resp.toString();
        });

    post("/users", "application/json", (req, res) -> {
      log.debug("createUser");
      User user = userParser.parse(req.body());
      User saved = Repository.save(userDAO).save(user);

      res.redirect(userUrlBuilder.build(saved), 303);
      return null;
    });

    get("/users/:name/inventories", "application/json", (req, res) ->
        {
          log.debug("allInventoriesForUser");
          res.type("application/json");
          User user = findUserBy.findBy(InMemoryLookers.userByName.lookFor(req.params("name")));
          return user.getInventories();
        }
      , (result) -> {
        JSONArray resp =
          new JSONArray(map((Collection<Inventory>) result, (i) -> new JSONObject(inventorySerialiser.serialise(i))));
        return resp.toString();
      });

    get("/users/:name", "application/json", (req, res) -> {
      log.debug("findLatestUser");
      User user = findUserBy.findBy(InMemoryLookers.userByName.lookFor(req.params("name")));

      res.redirect(userUrlBuilder.build(user), 303);
      return null;
    });

    get("/users/:name/:id", "application/json", (req, res) -> {
          log.debug("getUser");
          res.type("application/json");
          return userById.findById(Long.parseLong(req.params("id")));
        },
        (result) -> userSerialiser.serialise((User) result));

    get("/users/:name/inventories/:category", "application/json", (req, res) -> {
      log.debug("getInventoryForUser");
      User user = findUserBy.findBy(InMemoryLookers.userByName.lookFor(req.params("name")));

      Inventory inv =
        first(filter(user.getInventories(), (i) -> i.getCategory().getFullname().equals(req.params("category"))));

      res.redirect(inventoryUrlBuilder.build(inv), 303);

      return null;
    });

    post("/users/:name/inventories/:category", "application/json", (req, res) -> {
      log.debug("updateInventoryForUser");
      Repository.Save<Inventory> saver = Repository.save(inventoryDAO);
      Repository.Save<User> userSaver = Repository.save(userDAO);

      User user = findUserBy.findBy(InMemoryLookers.userByName.lookFor(req.params("name")));

      user.getInventories().removeIf((inv) -> inv.getCategory().getFullname().equals(req.params("category")));
      Inventory inv = inventoryParser.parse(req.body());
      Inventory saved = saver.save(inv);
      user.getInventories().add(saved);

      userSaver.save(user);

      res.redirect(inventoryUrlBuilder.build(saved), 303);

      return null;
    });
  }
}

