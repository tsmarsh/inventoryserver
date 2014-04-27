package com.tailoredshapes.inventoryserver.encoders;

import com.tailoredshapes.inventoryserver.security.LongHash;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;

import javax.inject.Inject;

public class LongHashEncoder <T> implements Encoder<T, LongHash>{

    Serialiser<T, String> serialiser;

    @Inject
    public LongHashEncoder(Serialiser<T, String> serialiser) {
        this.serialiser = serialiser;
    }

    @Override
    public Long encode(T object) {

        char[] value = serialiser.serialise(object).toCharArray();

        long h = 0l;

        if (value.length > 0) {
            for (char aValue : value) {
                h = 524287l * h + aValue;
            }
        }
        return h;
    }
}
