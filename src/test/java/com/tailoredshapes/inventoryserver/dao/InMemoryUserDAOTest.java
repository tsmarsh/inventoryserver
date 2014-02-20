package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.User;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InMemoryUserDAOTest {
    Long testId = 1l;
    private byte[] testSerializedUser = new byte[0];

    private Serializer<User> serializer = new Serializer<User>() {
        @Override
        public byte[] serialize(User object) {
            return testSerializedUser;
        }
    };

    private Encoder encoder = new Encoder() {
        @Override
        public Long encode(User user, byte[] bits) {
            return testId;
        }
    };

    @Test
    public void shouldUpdateAnObject(){
        InMemoryUserDAO dao = new InMemoryUserDAO(serializer, encoder);
        User user = new User();
        User returnedUser = dao.create(user);
        assertEquals(testId, returnedUser.getId());

        User updatedUser = new User().setId(testId).setName("Brian");
        dao.update(updatedUser);

        User lookupModel = new User();
        lookupModel.setId(testId);

        user = dao.read(lookupModel);
        assertEquals("Brian", user.getName());
    }

    @Test
    public void shouldDeleteAnObject(){
        InMemoryUserDAO dao = new InMemoryUserDAO(serializer, encoder);
        User user = new User();
        User returnedUser = dao.create(user);
        assertEquals(testId, returnedUser.getId());

        User fatedUser = new User().setId(testId);
        User lastChanceUser = dao.delete(fatedUser);

        assertEquals(lastChanceUser, user);

        User lookupUser = new User();
        lookupUser.setId(testId);

        returnedUser = dao.read(lookupUser);
        assertNull(returnedUser);
    }
}
