package com.tailoredshapes.inventoryserver.responders;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;

import java.io.IOException;
import java.io.OutputStream;

@RequestScoped
public class JSONResponder<T> implements Responder<T> {
    private final Serialiser<T> serialiser;

    @Inject
    public JSONResponder(Serialiser<T> serialiser) {
        this.serialiser = serialiser;
    }

    @Override
    public String respond(T object, OutputStream responseBody) {
        byte[] serialise = serialiser.serialise(object);
        try {
            responseBody.write(serialise);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(serialise);
    }
}