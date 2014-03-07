package com.tailoredshapes.inventoryserver.dao.memory;

import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.TestModel;
import com.tailoredshapes.inventoryserver.security.TestAlgorithm;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InMemoryDAOTest {

    private TestModel testModel;
    private Long testId = -1l;

    private Encoder<TestModel, TestAlgorithm> encoder = (testModel) -> testId;
    private InMemoryDAO<TestModel, TestAlgorithm> dao;

    @Before
    public void setUp() {
        testModel = new TestModel();
        testModel.setValue("Twifty");
        dao = new InMemoryDAO<TestModel, TestAlgorithm>(encoder) {
            @Override
            public TestModel saveChildren(TestModel object) {
                return object;
            }
        };
    }

    @Test
    public void shouldCreateAndReadAnObject() {

        TestModel model = dao.create(testModel);
        assertEquals(testId, model.getId());

        TestModel lookupModel = new TestModel();
        lookupModel.setId(testId);

        model = dao.read(lookupModel);
        assertEquals(testModel, model);

    }

    @Test
    public void shouldUpdateAnObject() {
        TestModel model = dao.create(testModel);
        assertEquals(testId, model.getId());

        TestModel updatedModel = new TestModel().setId(testId).setValue("New Value");
        dao.update(updatedModel);

        TestModel lookupModel = new TestModel();
        lookupModel.setId(testId);

        model = dao.read(lookupModel);
        assertEquals("New Value", model.getValue());
    }

    @Test
    public void shouldDeleteAnObject() {
        TestModel model = dao.create(testModel);
        assertEquals(testId, model.getId());

        TestModel fatedModel = new TestModel().setId(testId);
        TestModel lastChanceModel = dao.delete(fatedModel);

        assertEquals(testModel, lastChanceModel);

        TestModel lookupModel = new TestModel();
        lookupModel.setId(testId);

        model = dao.read(lookupModel);
        assertNull(model);
    }
}
