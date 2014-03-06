package com.tailoredshapes.inventoryserver.model;

import java.security.PrivateKey;

public interface Keyed {
    PrivateKey getPrivateKey();
}
