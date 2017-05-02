package com.tailoredshapes.inventoryserver.serialisers;

import com.tailoredshapes.inventoryserver.model.Category;

import org.json.JSONObject;

public class CategoryStringSerialiser implements Serialiser<Category> {
  @Override
  public String serialise(Category object) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("id", object.getId());
    jsonObject.put("name", object.getName());
    jsonObject.put("fullname", object.getFullname());
    if (object.getParent() != null) {
      jsonObject.put("parent", serialise(object.getParent()));
    }
    return jsonObject.toString();
  }
}
