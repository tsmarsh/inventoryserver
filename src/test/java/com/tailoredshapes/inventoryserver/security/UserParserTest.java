package com.tailoredshapes.inventoryserver.security;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.HashSet;
import java.util.Set;

import static com.tailoredshapes.inventoryserver.HibernateTest.hibernateInjector;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class UserParserTest {

    private SimpleScope scope;

    @After
    public void tearDown() throws Exception {
        scope.exit();
    }

    @Test
    public void testParseNewUserInMemory() throws Exception {
        scope = GuiceTest.injector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), new User().setId(141211l));
        scope.seed(Key.get(Inventory.class, Names.named("current_inventory")), new InventoryBuilder().build());
        testParseNewUser(GuiceTest.injector);
    }

    @Test
    public void testParseNewUserHibernate() throws Exception {
        scope = hibernateInjector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), new User().setId(141211l));
        scope.seed(Key.get(Inventory.class, Names.named("current_inventory")), new InventoryBuilder().build());
        EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();

        testParseNewUser(hibernateInjector);
        transaction.rollback();
    }

    public void testParseNewUser(Injector injector) throws Exception {
        JSONObject userJSON = new JSONObject().put("name", "Archer");

        Parser<User> userParser = injector.getInstance(new Key<Parser<User>>() {});

        User parsedUser = userParser.parse(userJSON.toString());
        assertEquals("Archer", parsedUser.getName());
    }


    @Test
    public void testParseExistingUserInMemory() throws Exception {
        scope = GuiceTest.injector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), new User().setId(141211l));


        testParseExistingUser(GuiceTest.injector);
    }

    @Test
    public void testParseExistingUserHibernate() throws Exception {
        scope = hibernateInjector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), new User().setId(141211l));


        EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();
        testParseExistingUser(hibernateInjector);
        transaction.rollback();
    }

    public void testParseExistingUser(Injector injector) throws Exception {
        Inventory inventory = new InventoryBuilder().id(null).build();
        scope.seed(Key.get(Inventory.class, Names.named("current_inventory")), inventory);
        Set<Inventory> inventories = new HashSet<>();
        inventories.add(inventory);

        User existingUser = new UserBuilder().id(null).name("Cassie").inventories(inventories).build();
        DAO<User> userDAO = injector.getInstance(new Key<DAO<User>>() {});

        User savedUser = userDAO.create(existingUser);
        savedUser.setName("Archer");

        Serialiser<User, String> serializer = injector.getInstance(new Key<Serialiser<User, String>>() {});

        String userJsonString = serializer.serialise(savedUser);


        UserParser userParser = injector.getInstance(UserParser.class);

        User parsedUser = userParser.parse(userJsonString);

        assertEquals("Archer", parsedUser.getName());
        assertTrue(parsedUser.getInventories().iterator().next().getId().equals(inventory.getId()));
        assertArrayEquals(savedUser.getPrivateKey().getEncoded(), parsedUser.getPrivateKey().getEncoded());
        assertArrayEquals(savedUser.getPublicKey().getEncoded(), parsedUser.getPublicKey().getEncoded());
    }


    @Test
    public void testParseUpdatedUserInMemory() throws Exception {
        scope = GuiceTest.injector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), new User().setId(141211l));
        testParseUpdatedUser(GuiceTest.injector, new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Test
    public void testParseUpdatedUserHibernate() throws Exception {
        scope = hibernateInjector.getInstance(SimpleScope.class);
        scope.enter();
        scope.seed(Key.get(User.class, Names.named("current_user")), new User().setId(141211l));

        final EntityManager manager = hibernateInjector.getInstance(EntityManager.class);
        EntityTransaction transaction = manager.getTransaction();

        transaction.begin();

        testParseUpdatedUser(hibernateInjector, new Runnable() {
            @Override
            public void run() {
                manager.flush();
            }
        });

        transaction.rollback();
    }

    public void testParseUpdatedUser(Injector injector, Runnable transactionCompleter) throws Exception {
        Inventory inventory = new InventoryBuilder().id(null).build();
        scope.seed(Key.get(Inventory.class, Names.named("current_inventory")), inventory);
        Set<Inventory> inventories = new HashSet<>();
        inventories.add(inventory);

        User existingUser = new UserBuilder().id(null).name("Cassie").inventories(inventories).build();
        DAO<User> userDAO = injector.getInstance(new Key<DAO<User>>() {});

        User savedUser = userDAO.create(existingUser);

        transactionCompleter.run();

        User clone = savedUser.clone();

        Category fullname = new CategoryBuilder().name(null).fullname("another.test").build();

        Inventory newInventory = new InventoryBuilder().id(null).category(fullname).build();
        clone.setName("Archer");

        Set<Inventory> inventorySet = new HashSet<>();
        inventorySet.add(inventory);
        inventorySet.add(newInventory);

        clone.setInventories(inventorySet);

        assertFalse(savedUser.hashCode() == clone.hashCode());

        Serialiser<User, String> serializer = injector.getInstance(new Key<Serialiser<User, String>>() {});

        String userJsonString = serializer.serialise(clone);

        UserParser userParser = injector.getInstance(UserParser.class);

        User parsedUser = userParser.parse(userJsonString);

        assertEquals("Archer", parsedUser.getName());
        assertThat(parsedUser.getInventories(), hasItems(newInventory, inventory));
        assertArrayEquals(clone.getPrivateKey().getEncoded(), parsedUser.getPrivateKey().getEncoded());
        assertArrayEquals(clone.getPublicKey().getEncoded(), parsedUser.getPublicKey().getEncoded());
    }
}
