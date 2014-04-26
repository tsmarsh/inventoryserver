package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.GuiceTest;
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

import java.util.Map;

import static com.tailoredshapes.inventoryserver.GuiceTest.*;
import static com.tailoredshapes.inventoryserver.HibernateTest.hibernateInjector;
import static org.junit.Assert.assertEquals;

public class HibernateCategoryRepositoryTest {
    private SimpleScope scope;

    @Test
    public void memoryFindByName() throws Exception {
        Repository<Category, Map<Long, Category>> repo = injector.getInstance(new Key<Repository<Category, Map<Long, Category>>>(){});
        FinderFactory<Category, String, Map<Long, Category>> findByFullName= injector.getInstance(new Key<FinderFactory<Category, String, Map<Long, Category>>>(){});

        testFindByName(injector, repo, findByFullName, new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Test
    public void hibernateFindByName() throws Exception {
        final EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();

        Repository<Category, EntityManager> repo = hibernateInjector.getInstance(new Key<Repository<Category, EntityManager>>(){});
        FinderFactory<Category, String, EntityManager> findByFullName= hibernateInjector.getInstance(new Key<FinderFactory<Category, String, EntityManager>>(){});

        testFindByName(hibernateInjector, repo, findByFullName, new Runnable() {
            @Override
            public void run() {
                manager.flush();
                manager.clear();
            }
        });

        transaction.rollback();
    }

    public <T> void testFindByName(Injector injector,Repository<Category, T> repo, FinderFactory<Category, String, T> findByFullName, Runnable reset) throws Exception {
        scope = injector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), new User());

        Category category = new CategoryBuilder().build();
        DAO<Category> dao = injector.getInstance(new Key<DAO<Category>>() {});
        Category savedCategory = dao.create(category);

        reset.run();

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

        testMissByName(repo, findByFullName);

        transaction.rollback();
        scope.exit();
    }


    @Test
    public void memoryMissByName() throws Exception {
        scope = injector.getInstance(SimpleScope.class);
        scope.enter();

        Repository<Category, Map<Long, Category>> repo = injector.getInstance(new Key<Repository<Category, Map<Long, Category>>>(){});
        FinderFactory<Category, String, Map<Long, Category>> findByFullName= injector.getInstance(new Key<FinderFactory<Category, String, Map<Long, Category>>>(){});

        testMissByName(repo, findByFullName);

        scope.exit();
    }

    public <T> void testMissByName(Repository<Category, T> repo, FinderFactory<Category, String, T> findByFullName ) throws Exception {
        scope.seed(Key.get(User.class, Names.named("current_user")), new User());

        Category byId = repo.findBy(findByFullName.lookFor("brian"));
        assertEquals("brian", byId.getFullname());
    }
}
