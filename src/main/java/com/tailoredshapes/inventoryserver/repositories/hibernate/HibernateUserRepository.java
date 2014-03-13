package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class HibernateUserRepository implements UserRepository {

    private SessionFactory sessionFactory;

    @Inject
    public HibernateUserRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User findById(long user_id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return (User) currentSession.get(User.class, user_id);
    }
}
