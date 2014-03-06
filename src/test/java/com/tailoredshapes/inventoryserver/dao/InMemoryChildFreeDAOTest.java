package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.TestModel;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
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
public class InMemoryChildFreeDAOTest {
    Long testId = 1l;

    private TestModel model;

    private Encoder<TestModel, TestAlgorithm> encoder = (model) -> testId;

    @Test
    public void shouldUpdateAnObject() {
        InMemoryDAO<TestModel, TestAlgorithm> dao = new InMemoryChildFreeDAO<>(encoder);
        model = new TestModel().setValue("twifty");
        TestModel returnedTestModel = dao.create(model);

        assertEquals(testId, returnedTestModel.getId());
        assertEquals("twifty", returnedTestModel.getValue());

        TestModel updatedModel = new TestModel().setId(testId).setValue("eleventy");
        dao.update(updatedModel);

        TestModel lookupModel = new TestModel().setId(testId);

        TestModel readModel = dao.read(lookupModel);
        assertEquals("eleventy", readModel.getValue());
    }

    @Test
    public void shouldDeleteAnObject() {
        InMemoryDAO<TestModel, TestAlgorithm> dao = new InMemoryChildFreeDAO<>(encoder);
        model = new TestModel().setValue("twifty");
        TestModel returnedTestModel = dao.create(model);

        assertEquals(testId, returnedTestModel.getId());
        assertEquals("twifty", returnedTestModel.getValue());

        TestModel deleteModel = new TestModel().setId(testId);
        dao.delete(deleteModel);

        TestModel lookupModel = new TestModel().setId(testId);

        assertNull(dao.read(lookupModel));
    }
}
