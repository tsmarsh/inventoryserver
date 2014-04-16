package com.tailoredshapes.inventoryserver.repositories.memory;

import javax.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import com.tailoredshapes.inventoryserver.security.Algorithm;

import java.util.Map;

public class InMemoryCategoryRepository<R extends Algorithm> implements CategoryRepository {

    private final Map<Long, Category> db;

    @Inject
    public InMemoryCategoryRepository(Map<Long, Category> db) {
        this.db = db;
    }

    @Override
    public Category findByFullname(String categoryFullName) {
        for (Category cat : db.values()) {
            if (cat.getFullname().equals(categoryFullName)) {
                return cat;
            }
        }

        return new Category().setFullname(categoryFullName);
    }

    @Override
    public boolean exists(String categoryFullName) {
        for (Category cat : db.values()) {
            if (cat.getFullname().equals(categoryFullName)) {
                return true;
            }
        }

        return false;
    }
}
