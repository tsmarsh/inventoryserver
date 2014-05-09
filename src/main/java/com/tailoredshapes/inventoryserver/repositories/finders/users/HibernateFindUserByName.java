package com.tailoredshapes.inventoryserver.repositories.finders.users;

import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class HibernateFindUserByName implements FinderFactory<User, String, EntityManager>, Finder<User, EntityManager> {
    private String name;

    @Override
    public User find(EntityManager manager) {
        Query cq = manager.createQuery("select u from User u where u.name = :name")
                .setParameter("name", name);

        User type;
        try {
            type = (User) cq.getSingleResult();
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


