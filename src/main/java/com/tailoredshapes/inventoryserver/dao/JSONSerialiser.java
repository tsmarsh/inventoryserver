package com.tailoredshapes.inventoryserver.dao;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.IOException;
import java.security.Key;
import java.security.PrivateKey;

public class JSONSerialiser<T> implements Serialiser<T> {

    private final ObjectMapper mapper;

    public JSONSerialiser() {
        mapper = new ObjectMapper();
        SimpleModule securityModule = new SimpleModule("Security", new Version(1, 0, 0, null));
        securityModule.addSerializer(Key.class, new JsonSerializer<Key>() {
            @Override
            public void serialize(Key value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
                jgen.writeBinary(value.getEncoded());
            }
        });

        mapper.registerModule(securityModule);
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
