package com.tailoredshapes.inventoryserver.repositories.memory;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {

    private final DAO<User> dao;
    private Map<Long, User> db;

    @Inject
    public InMemoryUserRepository(DAO<User> dao, Map<Long, User> db) {
        this.dao = dao;
        this.db = db;
    }

    @Override
    public User findById(Long user_id) {
        User user = new User().setId(user_id);
        return dao.read(user);
    }

    @Override
    public Collection<User> list() {
        return db.values();
    }
}
