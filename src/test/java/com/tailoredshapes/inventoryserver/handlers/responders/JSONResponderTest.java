package com.tailoredshapes.inventoryserver.handlers.responders;

import com.tailoredshapes.inventoryserver.model.TestModel;
import com.tailoredshapes.inventoryserver.serialisers.JSONSerialiser;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

import static org.junit.Assert.assertEquals;

public class JSONResponderTest {
    OutputStream stream = new OutputStream() {
        StringBuilder bob = new StringBuilder();

        @Override
        public void write(int b) throws IOException {
            bob.append((char) b);
        }

        @Override
        public String toString() {
            return bob.toString();
        }
    };

    JSONSerialiser<TestModel> serialiser = new JSONSerialiser<>();

    @Test
    public void testShouldReturnWhatWasWrittenToTheOutputStream() throws Exception {
        TestModel archer = new TestModel().setId(555l).setValue("Archer");
        JSONResponder<TestModel> responder = new JSONResponder<>(serialiser);
        String response = responder.respond(archer, stream);
        assertEquals(response, stream.toString());
    }

    @Test
    public void testShouldReturnValidJSON() throws Exception {
        TestModel archer = new TestModel().setId(555l).setValue("Archer");
        JSONResponder<TestModel> responder = new JSONResponder<>(serialiser);
        String response = responder.respond(archer, stream);
        JSONObject jsonObject = new JSONObject(response);
        assertEquals(jsonObject.getLong("id"), 555l);
        assertEquals(jsonObject.getString("value"), "Archer");
    }
}
