package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryCategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryInventoryRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryMetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryUserRepository;
import com.tailoredshapes.inventoryserver.security.RSA;
import com.tailoredshapes.inventoryserver.security.SHA;

public class InMemoryModule implements Module{
    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<DAO<Inventory>>() {})
                .to(new TypeLiteral<InMemoryDAO<Inventory, RSA>>() {});

        binder.bind(new TypeLiteral<InMemoryDAO<Inventory, RSA>>() {})
                .in(Singleton.class);


        binder.bind(new TypeLiteral<DAO<User>>() {})
                .to(new TypeLiteral<InMemoryDAO<User, RSA>>() {});


        binder.bind(new TypeLiteral<InMemoryDAO<User, RSA>>() {})
                .in(Singleton.class);


        binder.bind(new TypeLiteral<DAO<Category>>() {})
                .to(new TypeLiteral<InMemoryDAO<Category, SHA>>() {});

        binder.bind(new TypeLiteral<InMemoryDAO<Category, SHA>>() {})
                .in(Singleton.class);


        binder.bind(new TypeLiteral<DAO<Metric>>() {})
                .to(new TypeLiteral<InMemoryDAO<Metric, SHA>>() {});

        binder.bind(new TypeLiteral<InMemoryDAO<Metric, SHA>>() {})
                .in(Singleton.class);

        binder.bind(new TypeLiteral<DAO<MetricType>>() {})
                .to(new TypeLiteral<InMemoryDAO<MetricType, SHA>>() {});

        binder.bind(new TypeLiteral<InMemoryDAO<MetricType, SHA>>() {})
                .in(Singleton.class);


        binder.bind(InventoryRepository.class)
                .to(new TypeLiteral<InMemoryInventoryRepository<RSA>>() {});

        binder.bind(new TypeLiteral<CategoryRepository>() {})
                .to(new TypeLiteral<InMemoryCategoryRepository<SHA>>() {});

        binder.bind(new TypeLiteral<UserRepository>() {})
                .to(InMemoryUserRepository.class);

        binder.bind(new TypeLiteral<MetricTypeRepository>() {})
                .to(new TypeLiteral<InMemoryMetricTypeRepository<SHA>>() {});
    }
}
