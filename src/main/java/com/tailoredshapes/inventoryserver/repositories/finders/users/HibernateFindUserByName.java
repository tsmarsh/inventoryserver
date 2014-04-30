package com.tailoredshapes.inventoryserver.repositories.finders.users;

import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class HibernateFindUserByName implements FinderFactory<User, String, EntityManager>, Finder<User, EntityManager> {
    private String name;

    @Override
    public User find(EntityManager manager) {
        CriteriaBuilder cb = manager.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.where(cb.equal(root.get("name"), name));

        User type;
        try {
            type = manager.createQuery(cq).getSingleResult();
        } catch (Exception e) {
            type = new User().setName(name);
        }
        return type;
    }

    @Override
    public Finder<User, EntityManager> lookFor(String strings) {
        this.name = strings;
        return this;
    }
}


