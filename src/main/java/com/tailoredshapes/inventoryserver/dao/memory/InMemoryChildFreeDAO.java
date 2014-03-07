package com.tailoredshapes.inventoryserver.dao.memory;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.security.Algorithm;

public class InMemoryChildFreeDAO<T extends Idable<T>, R extends Algorithm> extends InMemoryDAO<T, R> {

    @Inject
    public InMemoryChildFreeDAO(Encoder<T, R> encoder) {
        super(encoder);
    }

    @Override
    public T saveChildren(T object) {
        return object;
    }
}

