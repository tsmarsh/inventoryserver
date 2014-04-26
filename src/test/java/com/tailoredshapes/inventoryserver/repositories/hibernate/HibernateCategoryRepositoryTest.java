package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.tailoredshapes.inventoryserver.HibernateTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateCategoryRepositoryTest {
    private SimpleScope scope;

    @Test
    public void hibernateFindByName() throws Exception {
        final EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        testFindByName(hibernateInjector, new Runnable() {
            @Override
            public void run() {
                manager.flush();
                manager.clear();
            }
        });

        transaction.rollback();
    }

    public void testFindByName(Injector injector, Runnable reset) throws Exception {
        scope = injector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), new User());

        Category category = new CategoryBuilder().build();
        DAO<Category> dao = injector.getInstance(new Key<DAO<Category>>() {});
        Repository<Category, EntityManager> repo = injector.getInstance(new Key<Repository<Category, EntityManager>>(){});
        Category savedCategory = dao.create(category);

        reset.run();

        FinderFactory<Category, String, EntityManager> findByFullName= injector.getInstance(new Key<FinderFactory<Category, String, EntityManager>>(){});

        Category byId = repo.findBy(findByFullName.lookFor(savedCategory.getFullname()));
        assertEquals(savedCategory, byId);

        scope.exit();
    }


    @Test
    public void hibernateMissByName() throws Exception {
        scope = hibernateInjector.getInstance(SimpleScope.class);
        scope.enter();
        final EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        Repository<Category, EntityManager> repo = hibernateInjector.getInstance(new Key<Repository<Category, EntityManager>>(){});
        FinderFactory<Category, String, EntityManager> findByFullName= hibernateInjector.getInstance(new Key<FinderFactory<Category, String, EntityManager>>(){});

        testMissByName(hibernateInjector, repo, findByFullName);

        transaction.rollback();
        scope.exit();
    }

    public <T> void testMissByName(Injector injector, Repository<Category, T> repo, FinderFactory<Category, String, T> findByFullName ) throws Exception {
        scope.seed(Key.get(User.class, Names.named("current_user")), new User());

        Category byId = repo.findBy(findByFullName.lookFor("brian"));
        assertEquals("brian", byId.getFullname());
    }
}
