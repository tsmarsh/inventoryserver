package com.tailoredshapes.inventoryserver.utils;

import java.security.KeyPair;

public interface KeyProvider<T> {
    KeyPair generate();
}

