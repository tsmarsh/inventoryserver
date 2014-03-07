package com.tailoredshapes.inventoryserver.encoders;

import com.tailoredshapes.inventoryserver.security.Algorithm;

public interface Encoder<T, R extends Algorithm> {
    Long encode(T object);
}
