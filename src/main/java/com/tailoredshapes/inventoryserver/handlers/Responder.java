package com.tailoredshapes.inventoryserver.handlers;

import java.io.OutputStream;

public interface Responder<T> {
    String respond(T object, OutputStream responseBody);
}

