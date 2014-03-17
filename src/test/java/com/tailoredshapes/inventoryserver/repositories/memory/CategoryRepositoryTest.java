package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.Test;

import static com.tailoredshapes.inventoryserver.GuiceTest.injector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CategoryRepositoryTest {

    @Test
    public void testFindByNameInMemory() throws Exception {
        testFindByName(GuiceTest.injector);
    }

    @Test
    public void testFindByNameHibernate() throws Exception {
        SessionFactory instance = GuiceTest.hibernateInjector.getInstance(SessionFactory.class);
        Transaction transaction = instance.getCurrentSession().beginTransaction();
        testFindByName(GuiceTest.hibernateInjector);
        transaction.rollback();
    }

    public void testFindByName(Injector injector) throws Exception {
        Category category = new CategoryBuilder().build();
        DAO<Category> dao = injector.getInstance(new Key<DAO<Category>>() {});
        CategoryRepository repo = injector.getInstance(CategoryRepository.class);
        Category savedCategory = dao.create(category);
        Category byId = repo.findByFullname(savedCategory.getFullname());
        assertEquals(savedCategory, byId);
    }


    @Test
    public void testExistsInMemory() throws Exception {
        testExists(GuiceTest.injector);
    }

    @Test
    public void testExistsHibernate() throws Exception {
        SessionFactory instance = GuiceTest.hibernateInjector.getInstance(SessionFactory.class);
        Transaction transaction = instance.getCurrentSession().beginTransaction();
        testExists(GuiceTest.hibernateInjector);
        transaction.rollback();
    }

    public void testExists(Injector injector) throws Exception {
        Category category = new CategoryBuilder().build();
        DAO<Category> dao = injector.getInstance(new Key<DAO<Category>>() {});
        CategoryRepository repo = injector.getInstance(CategoryRepository.class);
        Category savedCategory = dao.create(category);
        assertTrue(repo.exists(savedCategory.getFullname()));
    }

}
