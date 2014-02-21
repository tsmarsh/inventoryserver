package com.tailoredshapes.inventoryserver.dao;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSONSerialiserTest {

    Serialiser<TestModel> serialiser = new Serialiser<TestModel>(){

        @Override
        public byte[] serialise(TestModel object) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", object.value);
            jsonObject.put("id", object.getId());
            return jsonObject.toString().getBytes();
        }
    };

    @Test
    public void testSerialiseMultipleObjectsWithCustomSerilizer() throws Exception {
        for(long i = 0; i < 500; i++){
            TestModel testModel = new TestModel()
                    .setId(i)
                    .setValue("Archer" + i);

            byte[] serialisedTestModel = serialiser.serialise(testModel);

            String jsonString = new String(serialisedTestModel);
            JSONObject jsonObject = new JSONObject(jsonString);

            assertEquals("Archer"+i, jsonObject.getString("value"));
            assertEquals(i, jsonObject.getLong("id"));
        }
    }


    @Test
    public void testSerialiseMultipleObjects() throws Exception {
        Serialiser<TestModel> serialiser = new JSONSerialiser<>();
        for(long i = 0; i < 500; i++){
            TestModel testModel = new TestModel()
                    .setId(i)
                    .setValue("Archer" + i);

            byte[] serialisedTestModel = serialiser.serialise(testModel);

            String jsonString = new String(serialisedTestModel);
            JSONObject jsonObject = new JSONObject(jsonString);

            assertEquals("Archer"+i, jsonObject.getString("value"));
            assertEquals(i, jsonObject.getLong("id"));
        }
    }
}
