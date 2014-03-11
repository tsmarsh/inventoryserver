package com.tailoredshapes.inventoryserver.dao.memory;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.User;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class UserDAOTest extends GuiceTest {

    @Test
    public void testSaveChildren() throws Exception {
        DAO<User> dao = injector.getInstance(new Key<DAO<User>>() {});
        User user = new User().setName("Archer");
        User savedUser = dao.create(user);
        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getPublicKey());
        assertNotNull(savedUser.getPrivateKey());
    }
}
