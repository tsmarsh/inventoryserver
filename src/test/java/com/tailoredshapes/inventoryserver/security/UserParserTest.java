package com.tailoredshapes.inventoryserver.security;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class UserParserTest {

    @Test
    public void testParseNewUserInMemory() throws Exception {
        testParseNewUser(GuiceTest.injector);
    }

    @Test
    public void testParseNewUserHibernate() throws Exception {
        SessionFactory instance = GuiceTest.hibernateInjector.getInstance(SessionFactory.class);
        Transaction transaction = instance.getCurrentSession().beginTransaction();
        testParseNewUser(GuiceTest.hibernateInjector);
        transaction.rollback();
        instance.getCurrentSession().close();
    }

    public void testParseNewUser(Injector injector) throws Exception {
        JSONObject userJSON = new JSONObject().put("name", "Archer");

        UserParser userParser = injector.getInstance(UserParser.class);

        User parsedUser = userParser.parse(userJSON.toString());
        assertEquals("Archer", parsedUser.getName());
    }


    @Test
    public void testParseExistingUserInMemory() throws Exception {
        testParseExistingUser(GuiceTest.injector);
    }

    @Test
    public void testParseExistingUserHibernate() throws Exception {
        SessionFactory instance = GuiceTest.hibernateInjector.getInstance(SessionFactory.class);
        Transaction transaction = instance.getCurrentSession().beginTransaction();
        testParseExistingUser(GuiceTest.hibernateInjector);
        transaction.rollback();
        instance.getCurrentSession().close();
    }

    public void testParseExistingUser(Injector injector) throws Exception {
        Inventory inventory = new InventoryBuilder().id(null).build();
        Set<Inventory> inventories = new HashSet<>();
        inventories.add(inventory);

        User existingUser = new UserBuilder().id(null).name("Cassie").inventories(inventories).build();
        DAO<User> userDAO = injector.getInstance(new Key<DAO<User>>() {});

        User savedUser = userDAO.create(existingUser);
        savedUser.setName("Archer");

        Serialiser<User> serializer = injector.getInstance(new Key<Serialiser<User>>() {});

        String userJsonString = new String(serializer.serialise(savedUser));


        UserParser userParser = injector.getInstance(UserParser.class);

        User parsedUser = userParser.parse(userJsonString);

        assertEquals("Archer", parsedUser.getName());
        assertTrue(parsedUser.getInventories().iterator().next().equals(inventory));
        assertArrayEquals(savedUser.getPrivateKey().getEncoded(), parsedUser.getPrivateKey().getEncoded());
        assertArrayEquals(savedUser.getPublicKey().getEncoded(), parsedUser.getPublicKey().getEncoded());
    }


    @Test
    public void testParseUpdatedUserInMemory() throws Exception {
        testParseUpdatedUser(GuiceTest.injector, () -> {});
    }

    @Test
    public void testParseUpdatedUserHibernate() throws Exception {
        SessionFactory instance = GuiceTest.hibernateInjector.getInstance(SessionFactory.class);
        Transaction transaction = instance.getCurrentSession().beginTransaction();
        testParseUpdatedUser(GuiceTest.hibernateInjector, () -> {
            instance.getCurrentSession().flush();
        });
        transaction.rollback();
        instance.getCurrentSession().close();
    }

    public void testParseUpdatedUser(Injector injector, Runnable transactionCompleter) throws Exception {
        Inventory inventory = new InventoryBuilder().id(null).build();
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

        Serialiser<User> serializer = injector.getInstance(new Key<Serialiser<User>>() {});

        String userJsonString = new String(serializer.serialise(clone));

        UserParser userParser = injector.getInstance(UserParser.class);

        User parsedUser = userParser.parse(userJsonString);

        assertEquals("Archer", parsedUser.getName());
        assertThat(parsedUser.getInventories(), hasItems(newInventory, inventory));
        assertArrayEquals(clone.getPrivateKey().getEncoded(), parsedUser.getPrivateKey().getEncoded());
        assertArrayEquals(clone.getPublicKey().getEncoded(), parsedUser.getPublicKey().getEncoded());
    }
}
