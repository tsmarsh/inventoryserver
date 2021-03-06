package com.tailoredshapes.inventoryserver.model.builders;

import com.tailoredshapes.inventoryserver.model.Category;

public class CategoryBuilder {
  Long id = null;
  String name = "test";
  String fullname = "test";
  Category parent = null;

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

  public CategoryBuilder parent(Category parent) {
    this.parent = parent;
    return this;
  }

  public Category build() {
    return new Category().setParent(parent).setId(id).setName(name).setFullname(fullname);
  }
}
