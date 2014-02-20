package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.model.Category;

/**
 * Created by tmarsh on 2/17/14.
 */
public interface CategoryRepository {
    Category findByFullname(String categoryFullName);
}
