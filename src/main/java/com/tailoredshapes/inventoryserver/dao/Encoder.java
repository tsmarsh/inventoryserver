package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.utils.Algorithm;

public interface Encoder<T, R extends Algorithm> {
    Long encode(T object);
}
