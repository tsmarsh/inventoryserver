package com.tailoredshapes.inventoryserver.handlers;

import com.tailoredshapes.inventoryserver.model.Inventory;

import java.io.OutputStream;

/**
 * Created by tmarsh on 2/16/14.
 */
public interface Responder<T> {
    String respond(T object, OutputStream responseBody);
}

