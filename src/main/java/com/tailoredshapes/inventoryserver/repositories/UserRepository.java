package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.model.User;

/**
 * Created by tmarsh on 2/17/14.
 */
public interface UserRepository {
    User findById(long user_id);
}
