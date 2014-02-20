package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.model.User;

public interface UserRepository {
    User findById(long user_id);
}

