package com.tailoredshapes.inventoryserver;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.serialisers.InventoryStringSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.MetricStringSerialiser;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import static com.tailoredshapes.underbar.UnderBar.*;
import org.json.JSONArray;
import static spark.Spark.*;

import javax.persistence.EntityManagerFactory;
import java.util.Collections;
import java.util.List;


public interface Router {
    static void route(EntityManagerFactory emf){
        get("/inventories", "application/json", (req, res) -> {
           res.type("application/json");
           return Collections.emptyList();
        }, (result) -> {
            InventoryStringSerialiser serialiser = new InventoryStringSerialiser(new InventoryUrlBuilder("http", "localhost", 5555), new MetricStringSerialiser());
            JSONArray resp = new JSONArray(map((List<Inventory>) result, serialiser::serialise));
            return resp.toString();
        });
    }
}
