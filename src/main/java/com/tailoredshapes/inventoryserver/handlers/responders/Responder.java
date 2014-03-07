package com.tailoredshapes.inventoryserver.handlers.responders;

import java.io.OutputStream;

public interface Responder<T> {
    String respond(T object, OutputStream responseBody);
}

