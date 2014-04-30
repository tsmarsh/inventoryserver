package com.tailoredshapes.inventoryserver;

import com.google.inject.*;
import com.tailoredshapes.inventoryserver.dao.CategorySaver;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.Saver;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.extractors.UserIdExtractor;
import com.tailoredshapes.inventoryserver.filters.TFilter;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.finders.categories.InMemoryFindCategoryByFullName;
import com.tailoredshapes.inventoryserver.repositories.finders.inventories.InMemoryFindInventoryById;
import com.tailoredshapes.inventoryserver.repositories.finders.metrictype.InMemoryFindMetricByName;
import com.tailoredshapes.inventoryserver.repositories.finders.users.InMemoryFindUserById;
import com.tailoredshapes.inventoryserver.repositories.finders.users.InMemoryFindUserByName;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryUserInventoryRepository;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryModule implements Module {
    @Override
    public void configure(Binder binder) {

        binder.bind(new TypeLiteral<Map<Long, User>>() {})
                .to(new TypeLiteral<ConcurrentHashMap<Long, User>>() {})
                .in(Singleton.class);

        binder.bind(new TypeLiteral<Map<Long, Inventory>>() {})
                .to(new TypeLiteral<ConcurrentHashMap<Long, Inventory>>() {})
                .in(Singleton.class);
        binder.bind(new TypeLiteral<Map<Long, Metric>>() {})
                .to(new TypeLiteral<ConcurrentHashMap<Long, Metric>>() {})
                .in(Singleton.class);

        binder.bind(new TypeLiteral<Map<Long, MetricType>>() {})
                .to(new TypeLiteral<ConcurrentHashMap<Long, MetricType>>() {})
                .in(Singleton.class);

        binder.bind(new TypeLiteral<Map<Long, Category>>() {})
                .to(new TypeLiteral<ConcurrentHashMap<Long, Category>>() {})
                .in(Singleton.class);

        binder.bind(new TypeLiteral<DAO<Inventory>>() {})
                .to(new TypeLiteral<InMemoryDAO<Inventory>>() {});

        binder.bind(new TypeLiteral<DAO<User>>() {})
                .to(new TypeLiteral<InMemoryDAO<User>>() {});


        binder.bind(new TypeLiteral<DAO<Category>>() {})
                .to(new TypeLiteral<InMemoryDAO<Category>>() {});


        binder.bind(new TypeLiteral<DAO<Metric>>() {})
                .to(new TypeLiteral<InMemoryDAO<Metric>>() {});

        binder.bind(new TypeLiteral<DAO<MetricType>>() {})
                .to(new TypeLiteral<InMemoryDAO<MetricType>>() {});


        binder.bind(new TypeLiteral<Repository<Inventory, ?>>() {})
                .to(new TypeLiteral<Repository<Inventory, Map<Long, Inventory>>>() {});

        binder.bind(new TypeLiteral<Repository<User, ?>>() {})
                .to(new TypeLiteral<Repository<User, Map<Long, User>>>() {});

        binder.bind(new TypeLiteral<Repository<Category, ?>>() {})
                .to(new TypeLiteral<Repository<Category, Map<Long, Category>>>() {});

        binder.bind(new TypeLiteral<Repository<Metric, ?>>() {})
                .to(new TypeLiteral<Repository<Metric, Map<Long, Metric>>>() {});

        binder.bind(new TypeLiteral<Repository<MetricType, ?>>() {})
                .to(new TypeLiteral<Repository<MetricType, Map<Long, MetricType>>>() {});

        binder.bind(new TypeLiteral<Repository<Inventory, Map<Long, Inventory>>>() {})
                .to(new TypeLiteral<InMemoryUserInventoryRepository>() {});

        binder.bind(new TypeLiteral<Repository<Category, Map<Long, Category>>>() {})
                .to(new TypeLiteral<InMemoryRepository<Category>>() {});

        binder.bind(new TypeLiteral<Repository<User, Map<Long, User>>>() {})
                .to(new TypeLiteral<InMemoryRepository<User>>() {});

        binder.bind(new TypeLiteral<Repository<Metric, Map<Long, Metric>>>() {})
                .to(new TypeLiteral<InMemoryRepository<Metric>>() {});

        binder.bind(new TypeLiteral<Repository<MetricType, Map<Long, MetricType>>>() {})
                .to(new TypeLiteral<InMemoryRepository<MetricType>>() {});

        binder.bind(new TypeLiteral<FinderFactory<Category, String, Map<Long, Category>>>() {})
                .to(InMemoryFindCategoryByFullName.class);

        binder.bind(new TypeLiteral<FinderFactory<MetricType, String, Map<Long, MetricType>>>() {})
                .to(InMemoryFindMetricByName.class);

        binder.bind(new TypeLiteral<FinderFactory<User, String, Map<Long, User>>>() {})
                .to(InMemoryFindUserByName.class);

        binder.bind(new TypeLiteral<FinderFactory<User, Long, Map<Long, User>>>() {})
                .to(InMemoryFindUserById.class);

        binder.bind(new TypeLiteral<FinderFactory<Inventory, Long, Map<Long, Inventory>>>() {})
                .to(InMemoryFindInventoryById.class);

        binder.bind(new TypeLiteral<Parser<Inventory>>() {})
                .to(new TypeLiteral<InventoryParser<Map<Long, Category>, Map<Long, MetricType>>>() {});

        binder.bind(new TypeLiteral<Saver<Category>>() {})
                .to(new TypeLiteral<CategorySaver<Map<Long, Category>>>() {});

        binder.bind(new TypeLiteral<TFilter<Long, User, ?>>() {})
                .to(new TypeLiteral<TFilter<Long, User, Map<Long, User>>>(){});

        binder.bind(new TypeLiteral<TFilter<String, User, ?>>() {})
                .to(new TypeLiteral<TFilter<String, User, Map<Long, User>>>(){});

        binder.bind(new TypeLiteral<TFilter<Long, Inventory, ?>>() {})
                .to(new TypeLiteral<TFilter<Long, Inventory, Map<Long, Inventory>>>(){});
    }

    @Provides
    @Singleton
    public TFilter<Long,User, Map<Long, User>> providesUserFromIdFilter(Provider<Parser<User>> parser,
                                                              Provider<IdExtractor<Long, User>> idExtractor,
                                                              Provider<Repository<User, Map<Long, User>>> repository,
                                                              Provider<FinderFactory<User, Long, Map<Long, User>>> finderFactory) {
        return new TFilter<>(parser, idExtractor, finderFactory, repository, User.class, "user");
    }

    @Provides
    @Singleton
    public TFilter<String,User, Map<Long, User>> providesUserFromNameFilter(Provider<Parser<User>> parser,
                                                                  Provider<IdExtractor<String, User>> idExtractor,
                                                                  Provider<Repository<User, Map<Long, User>>> repository,
                                                                  Provider<FinderFactory<User, String, Map<Long, User>>> finderFactory) {
        return new TFilter<>(parser, idExtractor, finderFactory, repository, User.class, "user");
    }

    @Provides
    @Singleton
    public TFilter<Long,Inventory, Map<Long, Inventory>> providesInventoryFromIdFilter(Provider<Parser<Inventory>> parser,
                                                                        Provider<IdExtractor<Long, Inventory>> idExtractor,
                                                                        Provider<Repository<Inventory, Map<Long, Inventory>>> repository,
                                                                        Provider<FinderFactory<Inventory, Long, Map<Long, Inventory>>> finderFactory) {
        return new TFilter<>(parser, idExtractor, finderFactory, repository, Inventory.class, "inventory");
    }
}
