package com.tailoredshapes.inventoryserver.responders;

import java.io.Writer;

public interface Responder<T> {
    String respond(T object, Writer writer);
}

