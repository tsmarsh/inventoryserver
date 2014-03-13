package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;

public class HibernateCategoryRepository implements CategoryRepository {

    @Override
    public Category findByFullname(String categoryFullName) {
        return null;
    }
}

