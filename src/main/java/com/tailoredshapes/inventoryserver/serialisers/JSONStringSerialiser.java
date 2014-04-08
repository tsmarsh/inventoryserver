package com.tailoredshapes.inventoryserver.serialisers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.security.Key;

public class JSONStringSerialiser<T> implements Serialiser<T, String> {

    private final ObjectMapper mapper;

    public JSONStringSerialiser() {
        mapper = new ObjectMapper();
        SimpleModule securityModule = new SimpleModule("Security", new Version(1, 0, 0, null));
        securityModule.addSerializer(Key.class, new JsonSerializer<Key>() {
            @Override
            public void serialize(Key value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeBinary(value.getEncoded());
            }
        });

        mapper.registerModule(securityModule);
    }

    @Override
    public String serialise(T object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
