package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.User;

public interface CategoryRepository {
    Category findByFullname(User user, String categoryFullName);
}

