package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.TestModel;
import com.tailoredshapes.inventoryserver.serialisers.JSONSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSONSerialiserTest {

    Serialiser<TestModel, String> serialiser = new Serialiser<TestModel, String>() {
        @Override
        public String serialise(TestModel object) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", object.getValue());
            jsonObject.put("id", object.getId());
            return jsonObject.toString();
        }
    };

    @Test
    public void testSerialiseMultipleObjectsWithCustomSerilizer() throws Exception {
        for (long i = 0; i < 500; i++) {
            TestModel testModel = new TestModel()
                    .setId(i)
                    .setValue("Archer" + i);

            String jsonString = new String(serialiser.serialise(testModel));
            JSONObject jsonObject = new JSONObject(jsonString);

            assertEquals("Archer" + i, jsonObject.getString("value"));
            assertEquals(i, jsonObject.getLong("id"));
        }
    }


    @Test
    public void testSerialiseMultipleObjects() throws Exception {
        Serialiser<TestModel, byte[]> serialiser = new JSONSerialiser<>();
        for (long i = 0; i < 500; i++) {
            TestModel testModel = new TestModel()
                    .setId(i)
                    .setValue("Archer" + i);

            byte[] serialisedTestModel = serialiser.serialise(testModel);

            String jsonString = new String(serialisedTestModel);
            JSONObject jsonObject = new JSONObject(jsonString);

            assertEquals("Archer" + i, jsonObject.getString("value"));
            assertEquals(i, jsonObject.getLong("id"));
        }
    }
}
