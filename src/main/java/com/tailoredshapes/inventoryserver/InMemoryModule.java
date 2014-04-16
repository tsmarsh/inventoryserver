package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Module;
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

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryModule implements Module {
    @Override
    public void configure(Binder binder) {

        binder.bind(new TypeLiteral<Map<Long, User>>() {}).to(new TypeLiteral<ConcurrentHashMap<Long, User>>() {}).in(Singleton.class);
        binder.bind(new TypeLiteral<Map<Long, Inventory>>() {}).to(new TypeLiteral<ConcurrentHashMap<Long, Inventory>>() {}).in(Singleton.class);
        binder.bind(new TypeLiteral<Map<Long, Metric>>() {}).to(new TypeLiteral<ConcurrentHashMap<Long, Metric>>() {}).in(Singleton.class);
        binder.bind(new TypeLiteral<Map<Long, MetricType>>() {}).to(new TypeLiteral<ConcurrentHashMap<Long, MetricType>>() {}).in(Singleton.class);
        binder.bind(new TypeLiteral<Map<Long, Category>>() {}).to(new TypeLiteral<ConcurrentHashMap<Long, Category>>() {}).in(Singleton.class);

        binder.bind(new TypeLiteral<DAO<Inventory>>() {})
                .to(new TypeLiteral<InMemoryDAO<Inventory, SHA>>() {});

        binder.bind(new TypeLiteral<DAO<User>>() {})
                .to(new TypeLiteral<InMemoryDAO<User, RSA>>() {});


        binder.bind(new TypeLiteral<DAO<Category>>() {})
                .to(new TypeLiteral<InMemoryDAO<Category, SHA>>() {});


        binder.bind(new TypeLiteral<DAO<Metric>>() {})
                .to(new TypeLiteral<InMemoryDAO<Metric, SHA>>() {});

        binder.bind(new TypeLiteral<DAO<MetricType>>() {})
                .to(new TypeLiteral<InMemoryDAO<MetricType, SHA>>() {});


        binder.bind(InventoryRepository.class)
                .to(new TypeLiteral<InMemoryInventoryRepository>() {});

        binder.bind(new TypeLiteral<CategoryRepository>() {})
                .to(new TypeLiteral<InMemoryCategoryRepository<SHA>>() {});

        binder.bind(new TypeLiteral<UserRepository>() {})
                .to(InMemoryUserRepository.class);

        binder.bind(new TypeLiteral<MetricTypeRepository>() {})
                .to(new TypeLiteral<InMemoryMetricTypeRepository<SHA>>() {});
    }
}
