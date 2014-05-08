package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.tailoredshapes.inventoryserver.HibernateTest.hibernateInjector;
import static org.fest.assertions.api.Assertions.assertThat;

public class HibernateInventoryRepositoryTest {
    private DAO<Inventory> inventoryDAO;
    private Repository<Inventory, EntityManager> repo;

    private SimpleScope scope;
    private User user;

    @Before
    public void setUp() throws Exception {
        user = new UserBuilder().id(null).name("Archer").build();
        scope = hibernateInjector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), user);
    }

    @After
    public void tearDown() throws Exception {
        scope.exit();
    }

    @Test
    public void testFindById() throws Exception {
        Inventory inventory = new InventoryBuilder().build();
        scope.seed(Key.get(Inventory.class, Names.named("current_inventory")), inventory);

        repo = hibernateInjector.getInstance(new Key<Repository<Inventory, EntityManager>>() {});
        inventoryDAO = hibernateInjector.getInstance(new Key<DAO<Inventory>>() {});
        EntityManager em = hibernateInjector.getInstance(EntityManager.class);

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Inventory savedInventory = inventoryDAO.create(inventory);
            em.flush();
            em.clear();
            Inventory byId = repo.findById(savedInventory.getId());
            assertThat(byId).isEqualTo(savedInventory);
        } finally {
            transaction.rollback();
        }

    }

    @Test
    public void testSave() throws Exception {
        Inventory inventory = new InventoryBuilder().id(null).build();
        scope.seed(Key.get(Inventory.class, Names.named("current_inventory")), inventory);

        repo = hibernateInjector.getInstance(new Key<Repository<Inventory, EntityManager>>() {});
        inventoryDAO = hibernateInjector.getInstance(new Key<DAO<Inventory>>() {});
        Repository<User, EntityManager> userRepository = hibernateInjector.getInstance(new Key<Repository<User, EntityManager>>() {});
        EntityManager em = hibernateInjector.getInstance(EntityManager.class);

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            Inventory savedInventory = repo.save(inventory);
            em.flush();
            em.clear();
            User foundUser = userRepository.findById(user.getId());
            assertThat(foundUser.getInventories()).containsOnly(savedInventory);
            assertThat(savedInventory.getId()).isNotNull();
        } finally {
            transaction.rollback();
        }
    }
}
