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

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.tailoredshapes.inventoryserver.GuiceTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateCategoryRepositoryTest {

    @Test
    public void testFindByName() throws Exception {
        EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        Category category = new CategoryBuilder().build();
        DAO<Category> dao = hibernateInjector.getInstance(new Key<DAO<Category>>() {});
        CategoryRepository repo = hibernateInjector.getInstance(CategoryRepository.class);
        Category savedCategory = dao.create(category);

        Category byId = repo.findByFullname(savedCategory.getFullname());
        assertEquals(savedCategory, byId);

        transaction.rollback();
    }

    @Test
    public void testMissByName() throws Exception {
        EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        CategoryRepository repo = hibernateInjector.getInstance(CategoryRepository.class);

        Category byId = repo.findByFullname("brian");
        assertEquals("brian", byId.getFullname());

        transaction.rollback();
    }
}
