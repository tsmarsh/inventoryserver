package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.Category;

public class InMemoryCategoryRepository implements CategoryRepository{

    private DAO<Category> dao;

    public InMemoryCategoryRepository(InMemoryDAO<Category> dao) {
        this.dao = dao;
    }

    @Override
    public Category findByFullname(String categoryFullName) {
        return null;
    }
}
