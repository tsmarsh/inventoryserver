package com.tailoredshapes.inventoryserver.repositories.finders.categories;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;

import java.util.Map;

public class InMemoryFindCategoryByFullName implements FinderFactory<Category, String, Map<Long, Category>>, Finder<Category, Map<Long, Category>> {

    String categoryFullName;

    @Override
    public Category find(Map<Long, Category> db) {
        for (Category cat : db.values()) {
            if (cat.getFullname().equals(categoryFullName)) {
                return cat;
            }
        }

        return new Category().setFullname(categoryFullName);
    }


    @Override
    public Finder<Category, Map<Long, Category>> lookFor(String categories) {
        this.categoryFullName = categories;
        return this;
    }
}
