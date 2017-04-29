package com.tailoredshapes.inventoryserver;

import java.util.List;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;

import org.json.JSONArray;
import org.json.JSONObject;

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
import static com.tailoredshapes.underbar.UnderBar.map;
import static spark.Spark.get;
import static spark.Spark.post;


public interface Router {
  static void route(EntityManagerFactory emf) {
    get("/inventories", "application/json", (req, res) -> {
      res.type("application/json");
      return persistent(emf, (em) -> inventoryList.apply(em).list());
    }, (result) -> {
      JSONArray resp =
        new JSONArray(map((List<Inventory>) result, (i) -> new JSONObject(inventorySerialiser.serialise(i))));
      return resp.toString();
    });

    post("/inventories", "application/json", (req, res) ->
      transactional(emf, (em) -> {
        DAO<Inventory> dao = inventoryDAO.apply(em);
        Parser<Inventory> parser = inventoryParser.apply(em);
        Inventory inventory = parser.parse(req.body());
        Inventory saved = Repository.save(dao).save(inventory);

        res.redirect(inventoryUrlBuilder.build(saved));
        return null;
      }));

    get("/inventories/:id", "application/json", (req, res) ->
      persistent(emf, (em) -> {
        res.type("application/json");
        Repository.FindById<Inventory> byId = HibernateRepository.findById(Inventory.class, em);
        return byId.findById(Long.parseLong(req.params("id")));
      }), (result) ->
          inventorySerialiser.serialise((Inventory) result)
    );

    get("/users", "application/json", (req, res) -> {
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
        DAO<User> dao = userDAO.apply(em);
        Parser<User> parser = userParser.apply(em);
        User user = parser.parse(req.body());
        User saved = Repository.save(dao).save(user);

        res.redirect(userUrlBuilder.build(saved));
        return null;
      }));

    get("/users/:name/:id", "application/json", (req, res) -> persistent(emf, (em) -> {
          res.type("application/json");
          Repository.FindById<User> byId = HibernateRepository.findById(User.class, em);
          return byId.findById(Long.parseLong(req.params("id")));
        }),
        (result) -> userSerialiser.serialise((User) result));
  }
}

