package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;

public class InMemoryUserRepository implements UserRepository {

    private final DAO<User> dao;

    @Inject
    public InMemoryUserRepository(DAO<User> dao) {
        this.dao = dao;
    }

    @Override
    public User findById(Long user_id) {
        User user = new User().setId(user_id);
        return dao.read(user);
    }
}
