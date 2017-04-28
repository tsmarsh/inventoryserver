package com.tailoredshapes.inventoryserver;

import java.util.List;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.Repository;

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
import static com.tailoredshapes.underbar.UnderBar.map;
import static spark.Spark.get;
import static spark.Spark.post;


public interface Router {
  static void route(EntityManagerFactory emf) {
    get("/inventories", "application/json", (req, res) -> {
      res.type("application/json");
      return persistent(emf, (em) -> inventoryList.apply(em).list());
    }, (result) -> {
      JSONArray resp = new JSONArray(map((List<Inventory>) result, (i) -> new JSONObject(inventorySerialiser.serialise(i))));
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
  }
}

