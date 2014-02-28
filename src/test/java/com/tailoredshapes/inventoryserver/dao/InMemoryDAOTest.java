package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.TestModel;
import com.tailoredshapes.inventoryserver.model.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InMemoryDAOTest {

    private User testUser;
    private TestModel testModel;
    private byte[] testSerializedModel = new byte[0];
    private Long testId = -1l;

    private Serialiser<TestModel> serialiser = object -> testSerializedModel;

    private Encoder encoder = (user, bits) -> testId;

    @Before
    public void setUp() {
        testModel = new TestModel();
        testModel.setValue("Twifty");
        testUser = new User();
    }

    @Test
    public void shouldCreateAndReadAnObject() {
        InMemoryDAO<TestModel> dao = new InMemoryDAO<>(serialiser, encoder);
        TestModel model = dao.create(testUser, testModel);
        assertEquals(testId, model.getId());

        TestModel lookupModel = new TestModel();
        lookupModel.setId(testId);

        model = dao.read(testUser, lookupModel);
        assertEquals(testModel, model);

    }

    @Test
    public void shouldUpdateAnObject() {
        InMemoryDAO<TestModel> dao = new InMemoryDAO<>(serialiser, encoder);
        TestModel model = dao.create(testUser, testModel);
        assertEquals(testId, model.getId());

        TestModel updatedModel = new TestModel().setId(testId).setValue("New Value");
        dao.update(testUser, updatedModel);

        TestModel lookupModel = new TestModel();
        lookupModel.setId(testId);

        model = dao.read(testUser, lookupModel);
        assertEquals("New Value", model.getValue());
    }

    @Test
    public void shouldDeleteAnObject() {
        InMemoryDAO<TestModel> dao = new InMemoryDAO<>(serialiser, encoder);
        TestModel model = dao.create(testUser, testModel);
        assertEquals(testId, model.getId());

        TestModel fatedModel = new TestModel().setId(testId);
        TestModel lastChanceModel = dao.delete(testUser, fatedModel);

        assertEquals(testModel, lastChanceModel);

        TestModel lookupModel = new TestModel();
        lookupModel.setId(testId);

        model = dao.read(testUser, lookupModel);
        assertNull(model);
    }
}
