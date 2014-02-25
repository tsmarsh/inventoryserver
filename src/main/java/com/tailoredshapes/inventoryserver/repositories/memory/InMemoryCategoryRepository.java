package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;

import java.util.Map;

public class InMemoryCategoryRepository implements CategoryRepository {

    private final InMemoryDAO<Category> dao;

    @Inject
    public InMemoryCategoryRepository(InMemoryDAO<Category> dao) {
        this.dao = dao;
    }

    @Override
    public Category findByFullname(User user, String categoryFullName) {
        Map<Long, Category> longCategoryMap = dao.db.get(user);
        for (Category cat : longCategoryMap.values()) {
            if (cat.getFullname().equals(categoryFullName)) {
                return cat;
            }
        }
        return null;
    }
}
