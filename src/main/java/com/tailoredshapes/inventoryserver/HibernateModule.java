package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.hibernate.HibernateDAO;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.repositories.*;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateCategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateInventoryRepository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateMetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateUserRepository;
import com.tailoredshapes.inventoryserver.security.RSA;
import com.tailoredshapes.inventoryserver.security.SHA;

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

        binder.bind(new TypeLiteral<Repository<Inventory>>() {})
                .to(HibernateInventoryRepository.class);


        binder.bind(InventoryRepository.class)
                .to(HibernateInventoryRepository.class);

        binder.bind(new TypeLiteral<CategoryRepository>() {})
                .to(new TypeLiteral<HibernateCategoryRepository>() {});

        binder.bind(UserRepository.class)
                .to(HibernateUserRepository.class);

        binder.bind(new TypeLiteral<Repository<User>>() {})
                .to(HibernateUserRepository.class);

        binder.bind(new TypeLiteral<MetricTypeRepository>() {})
                .to(new TypeLiteral<HibernateMetricTypeRepository>() {});
    }
}
