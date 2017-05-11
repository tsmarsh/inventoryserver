package com.tailoredshapes.inventoryserver.builders;

import com.tailoredshapes.inventoryserver.model.Category;

public class CategoryBuilder {
  Long id = null;
  String name = "test";
  String fullname = "test";

  public CategoryBuilder id(Long id) {
    this.id = id;
    return this;
  }

  public CategoryBuilder name(String name) {
    this.name = name;
    return this;
  }

  public CategoryBuilder fullname(String fullname) {
    this.fullname = fullname;
    return this;
  }

  public Category build() {
    return new Category().setId(id).setName(name).setFullname(fullname);
  }
}
