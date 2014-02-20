package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryDAOTest {

    private User testUser;
    private Model testModel;
    private byte[] testSerializedModel;
    private Long testId = -1l;

    @Mock private Serializer<Model> serializer;
    @Mock private Encoder encoder;

    private class Model implements Idable<Model>{

        Long id = 0l;
        String value;

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public Model setId(Long id) {
            this.id = id;
            return this;
        }

        public String getValue() {
            return value;
        }

        public Model setValue(String value) {
            this.value = value;
            return this;
        }
    }

    @Before
    public void setUp(){
        testModel = new Model();
        testModel.setValue("Twifty");
        testUser = new User();
        when(serializer.serialize(testModel)).thenReturn(testSerializedModel);
        when(encoder.encode(testUser, testSerializedModel)).thenReturn(testId);
    }

    @Test
    public void shouldCreateAndReadAnObject(){
        InMemoryDAO<Model> dao = new InMemoryDAO<>(serializer, encoder);
        Model model = dao.create(testUser, testModel);
        assertEquals(testId, model.getId());

        Model lookupModel = new Model();
        lookupModel.setId(testId);

        model = dao.read(testUser, lookupModel);
        assertEquals(testModel, model);

    }

    @Test
    public void shouldUpdateAnObject(){
        InMemoryDAO<Model> dao = new InMemoryDAO<>(serializer, encoder);
        Model model = dao.create(testUser, testModel);
        assertEquals(testId, model.getId());

        Model updatedModel = new Model().setId(testId).setValue("New Value");
        dao.update(testUser, updatedModel);

        Model lookupModel = new Model();
        lookupModel.setId(testId);

        model = dao.read(testUser, lookupModel);
        assertEquals("New Value", model.getValue());
    }

    @Test
    public void shouldDeleteAnObject(){
        InMemoryDAO<Model> dao = new InMemoryDAO<>(serializer, encoder);
        Model model = dao.create(testUser, testModel);
        assertEquals(testId, model.getId());

        Model fatedModel = new Model().setId(testId);
        Model lastChanceModel = dao.delete(testUser, fatedModel);

        assertEquals(testModel, lastChanceModel);

        Model lookupModel = new Model();
        lookupModel.setId(testId);

        model = dao.read(testUser, lookupModel);
        assertNull(model);
    }
}
