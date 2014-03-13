package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;

import static com.tailoredshapes.inventoryserver.GuiceTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateCategoryRepositoryTest {

    @Test
    public void testFindByName() throws Exception {
        SessionFactory sessionFactory = hibernateInjector.getInstance(SessionFactory.class);
        Session currentSession = sessionFactory.getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();

        Category category = new CategoryBuilder().build();
        DAO<Category> dao = hibernateInjector.getInstance(new Key<DAO<Category>>() {});
        CategoryRepository repo = hibernateInjector.getInstance(CategoryRepository.class);
        Category savedCategory = dao.create(category);
        Category byId = repo.findByFullname(savedCategory.getFullname());
        assertEquals(savedCategory, byId);

        transaction.rollback();
    }
}
