package com.tailoredshapes.inventoryserver.repositories.finders.categories;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class HibernateFindCategoryByFullName implements FinderFactory<Category, String, EntityManager>, Finder<Category, EntityManager> {

    private String categoryFullName;

    @Override
    public Category find(EntityManager manager) {
        Query query = manager.createQuery("select c from Category c where c.fullname = :fullname")
                             .setParameter("fullname", categoryFullName);

        Category cat;
        try {
            cat = manager.merge((Category) query.getSingleResult());
        } catch (Exception e) {
            cat = new Category().setFullname(categoryFullName);
        }
        return cat;
    }

    @Override
    public Finder<Category, EntityManager> lookFor(String categories) {
        this.categoryFullName = categories;
        return this;
    }
}

