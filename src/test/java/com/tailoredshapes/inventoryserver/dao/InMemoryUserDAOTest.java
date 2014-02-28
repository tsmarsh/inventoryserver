package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.utils.KeyProvider;
import com.tailoredshapes.inventoryserver.utils.RSA;
import com.tailoredshapes.inventoryserver.utils.TestAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryUserDAOTest {
    Long testId = 1l;
    private byte[] testSerializedUser = new byte[0];

    private Serialiser<User> serialiser = object -> testSerializedUser;

    private Encoder<TestAlgorithm> encoder = (user, bits) -> testId;

    @Mock
    private PublicKey publicKey;
    @Mock
    private PrivateKey privateKey;

    private KeyProvider<TestAlgorithm> keyProvider = () -> new KeyPair(publicKey, privateKey);

    @Test
    public void shouldUpdateAnObject() {
        InMemoryUserDAO<RSA> dao = new InMemoryUserDAO(serialiser, encoder, keyProvider);
        User user = new User();
        User returnedUser = dao.create(user);

        assertEquals(testId, returnedUser.getId());
        assertEquals(privateKey, returnedUser.getPrivateKey());
        assertEquals(publicKey, returnedUser.getPublicKey());

        User updatedUser = new User().setId(testId).setName("Brian");
        dao.update(updatedUser);

        User lookupModel = new User();
        lookupModel.setId(testId);

        user = dao.read(lookupModel);
        assertEquals("Brian", user.getName());
    }

    @Test
    public void shouldDeleteAnObject() {
        InMemoryUserDAO dao = new InMemoryUserDAO(serialiser, encoder, keyProvider);
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
