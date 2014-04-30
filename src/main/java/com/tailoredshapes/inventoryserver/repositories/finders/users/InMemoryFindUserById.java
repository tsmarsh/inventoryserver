package com.tailoredshapes.inventoryserver.repositories.finders.users;

import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;

import java.util.Map;

public class InMemoryFindUserById implements FinderFactory<User, Long, Map<Long, User>>, Finder<User, Map<Long, User>> {
    private Long id;

    @Override
    public User find(Map<Long, User> db) {
        return db.get(id);
    }

    @Override
    public Finder<User, Map<Long, User>> lookFor(Long ts) {
        this.id = ts;
        return this;
    }
}
