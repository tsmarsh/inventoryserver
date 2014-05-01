package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateUserInventoryRepository;

import javax.persistence.EntityManager;
import java.util.function.Predicate;

public class UserRootHibernateRepositoryModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<Repository<Category, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<Category>>() {});

        binder.bind(new TypeLiteral<Repository<User, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<User>>() {});

        binder.bind(new TypeLiteral<Repository<Metric, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<Metric>>() {});

        binder.bind(new TypeLiteral<Repository<MetricType, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<MetricType>>() {});
    }

    @Provides
    public Repository<Inventory, EntityManager> inventoryRepositoryProvider(EntityManager manager,
                                                                            TypeLiteral<Inventory> type,
                                                                            @Named("current_user") com.google.inject.Provider<User> parent,
                                                                            DAO<Inventory> dao, Repository<User, ?> parentRepo,
                                                                            Predicate<Inventory> filter) {
        return new HibernateUserInventoryRepository(manager, type, parent, dao, parentRepo, filter);
    }
}
