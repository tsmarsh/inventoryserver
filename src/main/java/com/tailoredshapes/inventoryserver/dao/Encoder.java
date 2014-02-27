package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.utils.Algorithm;

public interface Encoder<T extends Algorithm> {
    Long encode(User user, byte[] bits);
}
