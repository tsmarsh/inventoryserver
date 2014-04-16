package com.tailoredshapes.inventoryserver.repositories.hibernate;

import javax.inject.Inject;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;

public class HibernateUserRepository implements UserRepository {

    private final EntityManager manager;

    @Inject
    public HibernateUserRepository(EntityManager manager) {
        this.manager = manager;
    }

    @Override
    public User findById(Long user_id) {
        return manager.find(User.class, user_id);
    }

    @Override
    public Collection<User> list() {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery<User> cq = criteriaBuilder.createQuery(User.class);
        Root<User> from = cq.from(User.class);
        CriteriaQuery<User> all = cq.select(from);
        TypedQuery<User> query = manager.createQuery(all);
        return query.getResultList();

    }
}
