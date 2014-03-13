package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class HibernateCategoryRepository implements CategoryRepository {

    private SessionFactory sessionFactory;

    @Inject
    public HibernateCategoryRepository(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public Category findByFullname(String categoryFullName) {
        Session currentSession = sessionFactory.getCurrentSession();
        List<Category> matches = currentSession.createCriteria(Category.class).add(Restrictions.eq("fullname", categoryFullName)).list();
        if(matches.isEmpty()){
            return new Category().setFullname(categoryFullName);
        }
        return matches.get(0);
    }
}

