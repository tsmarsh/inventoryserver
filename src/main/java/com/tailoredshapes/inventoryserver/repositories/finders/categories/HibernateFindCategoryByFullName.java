package com.tailoredshapes.inventoryserver.repositories.finders.categories;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class HibernateFindCategoryByFullName implements FinderFactory<Category, String, EntityManager>, Finder<Category, EntityManager> {

    private String categoryFullName;

    @Override
    public Category find(EntityManager manager) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Category> cq = criteriaBuilder.createQuery(Category.class);

        Root<Category> root = cq.from(Category.class);
        cq.where(criteriaBuilder.equal(root.get("fullname"), categoryFullName));

        TypedQuery<Category> query = manager.createQuery(cq);

        Category cat;
        try {
            cat = query.getSingleResult();
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

