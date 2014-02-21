package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.JSONSerialiser;

import java.io.IOException;
import java.io.OutputStream;

public class JSONResponder<T> implements Responder<T>{
    private JSONSerialiser<T> serialiser;

    @Inject
    public JSONResponder(JSONSerialiser<T> serialiser) {
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
