package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import com.tailoredshapes.inventoryserver.security.Algorithm;

public class InMemoryCategoryRepository<R extends Algorithm> implements CategoryRepository {

    private final InMemoryDAO<Category, R> dao;

    @Inject
    public InMemoryCategoryRepository(InMemoryDAO<Category, R> dao) {
        this.dao = dao;
    }

    @Override
    public Category findByFullname(String categoryFullName) {
        for (Category cat : dao.db.values()) {
            if (cat.getFullname().equals(categoryFullName)) {
                return cat;
            }
        }

        return new Category().setFullname(categoryFullName);
    }

    @Override
    public boolean exists(String categoryFullName) {
        for (Category cat : dao.db.values()) {
            if (cat.getFullname().equals(categoryFullName)) {
                return true;
            }
        }

        return false;
    }
}
