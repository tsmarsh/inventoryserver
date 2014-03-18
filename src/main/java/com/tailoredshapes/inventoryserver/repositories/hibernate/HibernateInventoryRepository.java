package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;
import org.hibernate.SessionFactory;

public class HibernateInventoryRepository implements InventoryRepository {

    private final SessionFactory sessionFactory;

    @Inject
    public HibernateInventoryRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Inventory findById(Long id) {
        return (Inventory) sessionFactory.getCurrentSession().get(Inventory.class, id);
    }
}

