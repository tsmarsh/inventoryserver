package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.User;

import java.io.InputStream;

/**
 * Created by tmarsh on 2/16/14.
 */
public class CategoryDAO implements DAO<Category> {

    @Override
    public Category create(User user, Category object) {
        return null;
    }

    @Override
    public Category read(User user, Category object) {
        return null;
    }

    @Override
    public Category update(User user, Category object) {
        return null;
    }

    @Override
    public Category delete(User user, Category object) {
        return null;
    }

    public Category findByFullname(String valueAsString) {
        return null;
    }
}
