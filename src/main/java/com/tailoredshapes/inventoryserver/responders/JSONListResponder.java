package com.tailoredshapes.inventoryserver.responders;

import com.google.inject.servlet.RequestScoped;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

@RequestScoped
public class JSONListResponder<T> implements Responder<Collection<T>> {

    private final Serialiser<T, String> serialiser;
    private final String name;

    @Inject
    public JSONListResponder(Serialiser<T, String> serialiser, String name) {
        this.serialiser = serialiser;
        this.name = name;
    }

    @Override
    public String respond(Collection<T> collection, Writer writer) {
        JSONArray jsonArray = new JSONArray();

        for (T t : collection) {
            jsonArray.put(new JSONObject(serialiser.serialise(t)));
        }

        JSONObject response = new JSONObject();
        response.put(name, jsonArray);

        String responseString = response.toString();

        try {
            writer.write(responseString);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }
}
