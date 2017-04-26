package com.tailoredshapes.inventoryserver.serialisers;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

public class UserStringSerialiser implements Serialiser<User> {

  private final UrlBuilder<User> urlBuilder;
  private final Serialiser<Inventory> inventorySerialiser;

  public UserStringSerialiser(UrlBuilder<User> urlBuilder, Serialiser<Inventory> inventorySerialiser) {
    this.urlBuilder = urlBuilder;
    this.inventorySerialiser = inventorySerialiser;
  }

  @Override
  public String serialise(User user) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("id", urlBuilder.build(user));
    jsonObject.put("name", user.getName());

    JSONArray inventories = new JSONArray();
    for (Inventory inventory : user.getInventories()) {
      inventories.put(new JSONObject(inventorySerialiser.serialise(inventory)));
    }

    jsonObject.put("inventories", inventories);

    return jsonObject.toString();
  }
}
