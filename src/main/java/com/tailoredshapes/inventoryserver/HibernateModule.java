package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.tailoredshapes.inventoryserver.dao.CategorySaver;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.Saver;
import com.tailoredshapes.inventoryserver.dao.hibernate.HibernateDAO;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;
import com.tailoredshapes.inventoryserver.repositories.InventoryCategoryPredicate;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.finders.categories.HibernateFindByFullName;
import com.tailoredshapes.inventoryserver.repositories.finders.metrictype.HibernateFindByName;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateUserInventoryRepository;
import com.tailoredshapes.inventoryserver.security.RSA;
import com.tailoredshapes.inventoryserver.security.SHA;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.util.function.Predicate;

public class HibernateModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<DAO<Inventory>>() {})
                .to(new TypeLiteral<HibernateDAO<Inventory, SHA>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<Inventory, SHA>>() {});


        binder.bind(new TypeLiteral<DAO<User>>() {})
                .to(new TypeLiteral<HibernateDAO<User, RSA>>() {});


        binder.bind(new TypeLiteral<HibernateDAO<User, RSA>>() {});


        binder.bind(new TypeLiteral<DAO<Category>>() {})
                .to(new TypeLiteral<HibernateDAO<Category, SHA>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<Category, SHA>>() {});


        binder.bind(new TypeLiteral<DAO<Metric>>() {})
                .to(new TypeLiteral<HibernateDAO<Metric, SHA>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<Metric, SHA>>() {});

        binder.bind(new TypeLiteral<DAO<MetricType>>() {})
                .to(new TypeLiteral<HibernateDAO<MetricType, SHA>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<MetricType, SHA>>() {});


        binder.bind(new TypeLiteral<Repository<Category, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<Category>>() {});

        binder.bind(new TypeLiteral<Repository<User, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<User>>(){});

        binder.bind(new TypeLiteral<Repository<Metric, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<Metric>>(){});

        binder.bind(new TypeLiteral<Repository<MetricType, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<MetricType>>() {});

        binder.bind(new TypeLiteral<Predicate<Inventory>>(){})
                .to(InventoryCategoryPredicate.class);

        binder.bind(new TypeLiteral<FinderFactory<Category, String, EntityManager>>(){})
                .to(HibernateFindByFullName.class);

        binder.bind(new TypeLiteral<FinderFactory<MetricType, String, EntityManager>>(){})
                .to(HibernateFindByName.class);

        binder.bind(new TypeLiteral<Parser<Inventory>>() {})
                .to(new TypeLiteral<InventoryParser<EntityManager, EntityManager>>(){});

        binder.bind(new TypeLiteral<Saver<Category>>() {})
                .to(new TypeLiteral<CategorySaver<EntityManager>>(){});



        binder.bind(new TypeLiteral<Repository<Inventory, ?>>(){})
                .to(new TypeLiteral<Repository<Inventory, EntityManager>>() {});

        binder.bind(new TypeLiteral<Repository<User, ?>>(){})
                .to(new TypeLiteral<Repository<User, EntityManager>>() {});

        binder.bind(new TypeLiteral<Repository<Category, ?>>(){})
                .to(new TypeLiteral<Repository<Category, EntityManager>>() {});

        binder.bind(new TypeLiteral<Repository<Metric, ?>>(){})
                .to(new TypeLiteral<Repository<Metric, EntityManager>>() {});

        binder.bind(new TypeLiteral<Repository<MetricType, ?>>(){})
                .to(new TypeLiteral<Repository<MetricType, EntityManager>>() {});
    }

    @Provides
    public Repository<Inventory, EntityManager> inventoryRepositoryProvider(EntityManager manager,
                                                                            TypeLiteral<Inventory> type,
                                                                            @Named("current_user") com.google.inject.Provider<User> parent,
                                                                            DAO<Inventory> dao, Repository<User, ?> parentRepo,
                                                                            Predicate<Inventory> filter){
        return new HibernateUserInventoryRepository(manager, type, parent, dao, parentRepo, filter);
    }

}
