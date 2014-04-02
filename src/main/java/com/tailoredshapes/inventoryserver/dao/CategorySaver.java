package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Category;

public class CategorySaver extends Saver<Category> {

    private DAO<Category> categoryDAO;

    @Inject
    public CategorySaver(DAO<Category> categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public Category saveChildren(Category object) {
        if(null == object.getName()){
            String[] split = object.getFullname().split("\\.");

            object.setName(split[split.length - 1]);


        }

        upsert(object.getParent(), categoryDAO);
        return object;
    }
}
