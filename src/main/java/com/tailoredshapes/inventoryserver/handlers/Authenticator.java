package com.tailoredshapes.inventoryserver.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.model.User;

/**
 * Created by tmarsh on 2/16/14.
 */
public interface Authenticator {
    User authenticate(HttpExchange httpExchange);
}
