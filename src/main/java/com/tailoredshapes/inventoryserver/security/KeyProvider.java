package com.tailoredshapes.inventoryserver.security;

import java.security.KeyPair;

public interface KeyProvider<T> {
    KeyPair generate();
}

