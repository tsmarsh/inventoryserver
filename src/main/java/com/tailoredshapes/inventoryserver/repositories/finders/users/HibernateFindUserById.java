package com.tailoredshapes.inventoryserver.repositories.finders.users;

import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;

import javax.persistence.EntityManager;
import java.util.Map;

public class HibernateFindUserById implements FinderFactory<User, Long, EntityManager>, Finder<User, EntityManager> {
    private Long id;

    @Override
    public User find(EntityManager db) {
        return db.find(User.class, id);
    }

    @Override
    public Finder<User, EntityManager> lookFor(Long ts) {
        this.id = ts;
        return this;
    }
}

