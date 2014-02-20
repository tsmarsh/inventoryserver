package com.tailoredshapes.inventoryserver.dao;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JSONSerialiser<T> implements Serialiser<T> {

    private final ObjectMapper mapper;

    public JSONSerialiser() {
        mapper = new ObjectMapper();
    }

    @Override
    public byte[] serialise(T object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
