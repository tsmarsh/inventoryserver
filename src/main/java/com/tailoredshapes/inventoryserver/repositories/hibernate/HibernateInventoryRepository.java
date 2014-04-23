package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.google.inject.Provider;
import com.google.inject.name.Named;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.function.Predicate;

public class HibernateInventoryRepository implements InventoryRepository {


    private final EntityManager manager;
    private final Provider<User> currentUser;
    private final DAO<Inventory> dao;
    private final Repository<User> userRepo;

    @Inject
    public HibernateInventoryRepository(EntityManager manager, @Named("current_user") Provider<User> currentUser, DAO<Inventory> dao, Repository<User> userRepo) {
        this.manager = manager;
        this.currentUser = currentUser;
        this.dao = dao;
        this.userRepo = userRepo;
    }

    @Override
    public Inventory findById(Long id) {
        return manager.find(Inventory.class, id);
    }

    @Override
    public Collection<Inventory> list() {
        return currentUser.get().getInventories();
    }

    @Override
    public Inventory save(final Inventory inventory) {
        Inventory savedInventory = inventory.getId() == null ? dao.create(inventory) : dao.update(inventory);

        User user = currentUser.get();
        Collection<Inventory> inventories = user.getInventories();
        inventories.removeIf(new Predicate<Inventory>() {
            @Override
            public boolean test(Inventory underTest) {
                return inventory.getCategory().equals(underTest.getCategory());
            }
        });

        inventories.add(savedInventory);
        userRepo.save(user);
        return savedInventory;
    }
}

