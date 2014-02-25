package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.User;

public interface Encoder {
    Long encode(User user, byte[] bits);
}
