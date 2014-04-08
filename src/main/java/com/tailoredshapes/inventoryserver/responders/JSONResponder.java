package com.tailoredshapes.inventoryserver.responders;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;

import java.io.IOException;
import java.io.Writer;

@RequestScoped
public class JSONResponder<T> implements Responder<T> {
    private final Serialiser<T, String> serialiser;

    @Inject
    public JSONResponder(Serialiser<T, String> serialiser) {
        this.serialiser = serialiser;
    }

    @Override
    public String respond(T object, Writer writer) {
        String serialise = serialiser.serialise(object);
        try {
            writer.write(serialise);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serialise;
    }
}
