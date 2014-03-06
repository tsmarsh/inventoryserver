package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.model.Category;

public interface CategoryRepository {
    Category findByFullname(String categoryFullName);
}

