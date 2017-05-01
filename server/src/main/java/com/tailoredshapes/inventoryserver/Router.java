package com.tailoredshapes.inventoryserver;

import java.util.Collection;
import java.util.List;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateLookers;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static com.tailoredshapes.inventoryserver.Persistence.persistent;
import static com.tailoredshapes.inventoryserver.Persistence.transactional;
import static com.tailoredshapes.inventoryserver.Providers.inventoryDAO;
import static com.tailoredshapes.inventoryserver.Providers.inventoryList;
import static com.tailoredshapes.inventoryserver.Providers.inventoryParser;
import static com.tailoredshapes.inventoryserver.Providers.inventorySerialiser;
import static com.tailoredshapes.inventoryserver.Providers.inventoryUrlBuilder;
import static com.tailoredshapes.inventoryserver.Providers.userDAO;
import static com.tailoredshapes.inventoryserver.Providers.userList;
import static com.tailoredshapes.inventoryserver.Providers.userParser;
import static com.tailoredshapes.inventoryserver.Providers.userSerialiser;
import static com.tailoredshapes.inventoryserver.Providers.userUrlBuilder;
import static com.tailoredshapes.underbar.UnderBar.filter;
import static com.tailoredshapes.underbar.UnderBar.first;
import static com.tailoredshapes.underbar.UnderBar.map;
import static spark.Spark.get;
import static spark.Spark.post;


public interface Router {
  Logger log = LoggerFactory.getLogger(Router.class);

  static void route(EntityManagerFactory emf) {
    get("/inventories", "application/json", (req, res) -> {
      log.debug("allInventories");
      res.type("application/json");
      return persistent(emf, (em) -> inventoryList.apply(em).list());
    }, (result) -> {
      JSONArray resp =
        new JSONArray(map((List<Inventory>) result, (i) -> new JSONObject(inventorySerialiser.serialise(i))));
      return resp.toString();
    });

    post("/inventories", "application/json", (req, res) ->
      transactional(emf, (em) -> {
        log.debug("createInventory");
        DAO<Inventory> dao = inventoryDAO.apply(em);
        Parser<Inventory> parser = inventoryParser.apply(em);
        Inventory inventory = parser.parse(req.body());
        Inventory saved = Repository.save(dao).save(inventory);

        res.redirect(inventoryUrlBuilder.build(saved));
        return null;
      }));

    get("/inventories/:id", "application/json", (req, res) ->
      persistent(emf, (em) -> {
        log.debug("readInventory");
        res.type("application/json");
        Repository.FindById<Inventory> byId = HibernateRepository.findById(Inventory.class, em);
        return byId.findById(Long.parseLong(req.params("id")));
      }), (result) ->
          inventorySerialiser.serialise((Inventory) result)
    );

    get("/users", "application/json", (req, res) -> {
          log.debug("allUsers");
          res.type("application/json");
          return persistent(emf, (em) ->
            userList.apply(em).list());
        },
        (result) -> {
          JSONArray resp = new JSONArray(map((List<User>) result, (i) -> new JSONObject(userSerialiser.serialise(i))));
          return resp.toString();
        });

    post("/users", "application/json", (req, res) ->
      transactional(emf, (em) -> {
        log.debug("createUser");
        DAO<User> dao = userDAO.apply(em);
        Parser<User> parser = userParser.apply(em);
        User user = parser.parse(req.body());
        User saved = Repository.save(dao).save(user);

        res.redirect(userUrlBuilder.build(saved));
        return null;
      }));


    get("/users/:name/inventories", "application/json", (req, res) ->
          persistent(emf, (em) -> {
            log.debug("allInventoriesForUser");
            res.type("application/json");
            Repository.FindBy<User, EntityManager> findBy = HibernateRepository.findBy(em);
            User user = findBy.findBy(HibernateLookers.userByName.lookFor(req.params("name")));
            return user.getInventories();
          })
      , (result) -> {
        JSONArray resp =
          new JSONArray(map((Collection<Inventory>) result, (i) -> new JSONObject(inventorySerialiser.serialise(i))));
        return resp.toString();
      });


    get("/users/:name", "application/json", (req, res) -> transactional(emf, (em) -> {
      log.debug("findLatestUser");
      Repository.FindBy<User, EntityManager> findBy = HibernateRepository.findBy(em);

      User user = findBy.findBy(HibernateLookers.userByName.lookFor(req.params("name")));

      res.redirect(userUrlBuilder.build(user));
      return null;
    }));

    get("/users/:name/:id", "application/json", (req, res) ->
          persistent(emf, (em) -> {
            log.debug("getUser");
            res.type("application/json");
            Repository.FindById<User> byId = HibernateRepository.findById(User.class, em);
            return byId.findById(Long.parseLong(req.params("id")));
          }),
        (result) -> userSerialiser.serialise((User) result));

    get("/users/:name/inventories/:category", "application/json", (req, res) -> transactional(emf, (em) -> {
      log.debug("getInventoryForUser");
      Repository.FindBy<User, EntityManager> findBy = HibernateRepository.findBy(em);

      User user = findBy.findBy(HibernateLookers.userByName.lookFor(req.params("name")));

      Inventory inv =
        first(filter(user.getInventories(), (i) -> i.getCategory().getFullname().equals(req.params("category"))));

      res.redirect(inventoryUrlBuilder.build(inv));

      return null;
    }));

    post("/users/:name/inventories/:category", "application/json", (req, res) -> transactional(emf, (em) -> {
      log.debug("updateInventoryForUser");
      Repository.FindBy<User, EntityManager> findBy = HibernateRepository.findBy(em);
      DAO<Inventory> dao = inventoryDAO.apply(em);
      DAO<User> userDao = userDAO.apply(em);
      Repository.Save<Inventory> saver = Repository.save(dao);
      Repository.Save<User> userSaver = Repository.save(userDao);
      Parser<Inventory> parser = inventoryParser.apply(em);

      User user = findBy.findBy(HibernateLookers.userByName.lookFor(req.params("name")));

      user.getInventories().removeIf((inv) -> inv.getCategory().getFullname().equals(req.params("category")));
      Inventory inv = parser.parse(req.body());
      Inventory saved = saver.save(inv);
      user.getInventories().add(saved);

      userSaver.save(user);

      res.redirect(inventoryUrlBuilder.build(saved));

      return null;
    }));
  }
}

