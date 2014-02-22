package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.UserDAO;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;

public class InMemoryUserRepository implements UserRepository {

    private UserDAO dao;

    @Inject
    public InMemoryUserRepository(UserDAO dao) {
        this.dao = dao;
    }

    @Override
    public User findById(long user_id) {
        User user = new User().setId(user_id);
        return dao.read(user);
    }
}
