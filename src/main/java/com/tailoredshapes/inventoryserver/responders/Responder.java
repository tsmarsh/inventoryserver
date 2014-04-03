package com.tailoredshapes.inventoryserver.responders;

import java.io.OutputStream;

public interface Responder<T> {
    String respond(T object, OutputStream responseBody);
}

