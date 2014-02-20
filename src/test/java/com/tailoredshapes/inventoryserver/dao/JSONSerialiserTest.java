package com.tailoredshapes.inventoryserver.dao;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JSONSerialiserTest {
    @Test
    public void testSerialise() throws Exception {
        JSONSerialiser<TestModel> serialiser = new JSONSerialiser<>();
        TestModel testModel = new TestModel()
                                    .setId(121411l)
                                    .setValue("Archer");

        byte[] serialisedTestModel = serialiser.serialise(testModel);

        String jsonString = new String(serialisedTestModel);
        JSONObject jsonObject = new JSONObject(jsonString);

        assertEquals("Archer", jsonObject.getString("value"));
        assertEquals(121411l, jsonObject.getLong("id"));
    }
}
