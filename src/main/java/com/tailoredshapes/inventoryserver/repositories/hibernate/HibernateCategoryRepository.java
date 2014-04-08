package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class HibernateCategoryRepository implements CategoryRepository {


    private final EntityManager manager;

    @Inject
    public HibernateCategoryRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public Category findByFullname(String categoryFullName) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<Category> cq = criteriaBuilder.createQuery(Category.class);

        Root<Category> root = cq.from(Category.class);
        cq.where(criteriaBuilder.equal(root.get("fullname"), categoryFullName));

        TypedQuery<Category> query = manager.createQuery(cq);

        Category cat;
        try{
            cat = query.getSingleResult();
        }catch(Exception e){
            cat = new Category().setFullname(categoryFullName);
        }
        return cat;
    }

    @Override
    public boolean exists(String categoryFullName) {
        return null != findByFullname(categoryFullName);
    }
}

