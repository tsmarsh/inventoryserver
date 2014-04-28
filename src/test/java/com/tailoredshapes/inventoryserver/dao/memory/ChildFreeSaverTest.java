package com.tailoredshapes.inventoryserver.dao.memory;

import com.tailoredshapes.inventoryserver.dao.ChildFreeSaver;
import com.tailoredshapes.inventoryserver.dao.Saver;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.TestModel;
import com.tailoredshapes.inventoryserver.security.TestAlgorithm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ChildFreeSaverTest {
    Long testId = 1l;

    private TestModel model;

    private Encoder<TestModel, TestAlgorithm> encoder = new Encoder<TestModel, TestAlgorithm>() {
        @Override
        public Long encode(TestModel object) {
            return testId;
        }
    };

    private Saver<TestModel> saver = new ChildFreeSaver<>();

    @Test
    public void shouldUpdateAnObject() {
        Map<Long, TestModel> db = new HashMap<>();
        InMemoryDAO<TestModel> dao = new InMemoryDAO<>(db, encoder, saver);
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
        Map<Long, TestModel> db = new HashMap<>();
        InMemoryDAO<TestModel> dao = new InMemoryDAO<>(db, encoder, saver);
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
