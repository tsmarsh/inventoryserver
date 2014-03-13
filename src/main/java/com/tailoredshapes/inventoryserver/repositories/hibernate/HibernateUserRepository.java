package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;

public class HibernateUserRepository implements UserRepository {

    @Override
    public User findById(long user_id) {
        return null;
    }
}
