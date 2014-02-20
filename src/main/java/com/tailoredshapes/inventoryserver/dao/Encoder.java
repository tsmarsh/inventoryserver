package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.User;

/**
 * Created by tmarsh on 2/17/14.
 */
public interface Encoder {
    Long encode(User user, byte[] bits);
}
