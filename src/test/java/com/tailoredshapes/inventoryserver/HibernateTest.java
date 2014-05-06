package com.tailoredshapes.inventoryserver;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.RequestScoped;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.modules.*;
import com.tailoredshapes.inventoryserver.modules.jpa.HibernateModule;
import com.tailoredshapes.inventoryserver.modules.jpa.UserRootHibernateRepositoryModule;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateUserInventoryRepository;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;

import javax.inject.*;
import javax.persistence.EntityManager;
import java.util.function.Predicate;

public class HibernateTest {
    public static Injector hibernateInjector;

    static {
        hibernateInjector = Guice.createInjector(
                new JpaPersistModule("inventory_server"),
                new InventoryServerModule("localhost", 5555),
                new HibernateModule(),
                new JSONModule(),
                new UserRootUrlBuilders(),
                new EncoderModule(),
                new RandomShitModule(),
                new Module() {

                    @Override
                    public void configure(Binder binder) {
                        SimpleScope requestScope = new SimpleScope();
                        binder.bindScope(RequestScoped.class, requestScope);
                        binder.bind(SimpleScope.class).toInstance(requestScope);
                        binder.bind(User.class)
                                .annotatedWith(Names.named("current_user"))
                                .toProvider(SimpleScope.<User>seededKeyProvider())
                                .in(RequestScoped.class);
                        binder.bind(Inventory.class)
                                .annotatedWith(Names.named("current_inventory"))
                                .toProvider(SimpleScope.<Inventory>seededKeyProvider())
                                .in(RequestScoped.class);

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
                                                                                            @Named("current_user") javax.inject.Provider<User> parent,
                                                                                            DAO<Inventory> dao, Repository<User, ?> parentRepo,
                                                                                            Predicate<Inventory> filter) {
                        return new HibernateUserInventoryRepository(manager, type, parent, dao, parentRepo, filter);
                    }
                });

        PersistService instance = hibernateInjector.getInstance(PersistService.class);
        instance.start();

    }
}
