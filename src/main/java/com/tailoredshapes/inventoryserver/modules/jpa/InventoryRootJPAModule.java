package com.tailoredshapes.inventoryserver.modules.jpa;

import com.google.inject.*;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.dao.hibernate.HibernateDAO;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.encoders.RSAEncoder;
import com.tailoredshapes.inventoryserver.encoders.SHAEncoder;
import com.tailoredshapes.inventoryserver.extractors.*;
import com.tailoredshapes.inventoryserver.filters.TFilter;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;
import com.tailoredshapes.inventoryserver.repositories.InventoryCategoryPredicate;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.finders.categories.HibernateFindCategoryByFullName;
import com.tailoredshapes.inventoryserver.repositories.finders.inventories.HibernateFindInventoryById;
import com.tailoredshapes.inventoryserver.repositories.finders.metrictype.HibernateFindMetricTypeByName;
import com.tailoredshapes.inventoryserver.repositories.finders.users.HibernateFindUserById;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;
import com.tailoredshapes.inventoryserver.responders.JSONListResponder;
import com.tailoredshapes.inventoryserver.responders.JSONResponder;
import com.tailoredshapes.inventoryserver.responders.Responder;
import com.tailoredshapes.inventoryserver.security.KeyProvider;
import com.tailoredshapes.inventoryserver.security.RSA;
import com.tailoredshapes.inventoryserver.security.RSAKeyProvider;
import com.tailoredshapes.inventoryserver.serialisers.*;
import com.tailoredshapes.inventoryserver.servlets.Pestlet;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.validators.InventoryValidator;
import com.tailoredshapes.inventoryserver.validators.UserValidator;
import com.tailoredshapes.inventoryserver.validators.Validator;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.function.Predicate;

public class InventoryRootJPAModule extends PrivateModule {

    @Override
    protected void configure() {
        bind(new TypeLiteral<Repository<Category, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<Category>>() {});

        bind(new TypeLiteral<Repository<Inventory, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<Inventory>>() {});

        bind(new TypeLiteral<Repository<User, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<User>>() {});

        bind(new TypeLiteral<Repository<Metric, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<Metric>>() {});

        bind(new TypeLiteral<Repository<MetricType, EntityManager>>() {})
                .to(new TypeLiteral<HibernateRepository<MetricType>>() {});


        ///

        bind(new TypeLiteral<DAO<Inventory>>() {})
                .to(new TypeLiteral<HibernateDAO<Inventory>>() {});

        bind(new TypeLiteral<HibernateDAO<Inventory>>() {});


        bind(new TypeLiteral<DAO<User>>() {})
                .to(new TypeLiteral<HibernateDAO<User>>() {});


        bind(new TypeLiteral<HibernateDAO<User>>() {});


        bind(new TypeLiteral<DAO<Category>>() {})
                .to(new TypeLiteral<HibernateDAO<Category>>() {});

        bind(new TypeLiteral<HibernateDAO<Category>>() {});


        bind(new TypeLiteral<DAO<Metric>>() {})
                .to(new TypeLiteral<HibernateDAO<Metric>>() {});

        bind(new TypeLiteral<HibernateDAO<Metric>>() {});

        bind(new TypeLiteral<DAO<MetricType>>() {})
                .to(new TypeLiteral<HibernateDAO<MetricType>>() {});

        bind(new TypeLiteral<HibernateDAO<MetricType>>() {});

        bind(new TypeLiteral<FinderFactory<Category, String, EntityManager>>() {})
                .to(HibernateFindCategoryByFullName.class);

        bind(new TypeLiteral<FinderFactory<MetricType, String, EntityManager>>() {})
                .to(HibernateFindMetricTypeByName.class);

        bind(new TypeLiteral<FinderFactory<User, Long, EntityManager>>() {})
                .to(HibernateFindUserById.class);

        bind(new TypeLiteral<FinderFactory<Inventory, Long, EntityManager>>() {})
                .to(HibernateFindInventoryById.class);

        bind(new TypeLiteral<Parser<Inventory>>() {})
                .to(new TypeLiteral<InventoryParser<EntityManager, EntityManager>>() {});

        bind(new TypeLiteral<Saver<Category>>() {})
                .to(new TypeLiteral<CategorySaver<EntityManager>>() {});


        bind(new TypeLiteral<Repository<Inventory, ?>>() {})
                .to(new TypeLiteral<Repository<Inventory, EntityManager>>() {});

        bind(new TypeLiteral<Repository<User, ?>>() {})
                .to(new TypeLiteral<Repository<User, EntityManager>>() {});

        bind(new TypeLiteral<Repository<Category, ?>>() {})
                .to(new TypeLiteral<Repository<Category, EntityManager>>() {});

        bind(new TypeLiteral<Repository<Metric, ?>>() {})
                .to(new TypeLiteral<Repository<Metric, EntityManager>>() {});

        bind(new TypeLiteral<Repository<MetricType, ?>>() {})
                .to(new TypeLiteral<Repository<MetricType, EntityManager>>() {});

        bind(new TypeLiteral<TFilter<Long, Inventory, ?>>() {}).annotatedWith(Names.named("inventory_root"))
                .to(Key.get(new TypeLiteral<TFilter<Long, Inventory, EntityManager>>() {}));

        expose(new TypeLiteral<TFilter<Long, Inventory, ?>>() {}).annotatedWith(Names.named("inventory_root"));

        bind(new TypeLiteral<Encoder<User, ?>>() {})
                .to(new TypeLiteral<RSAEncoder<User>>() {});

        bind(new TypeLiteral<Encoder<Inventory, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<Inventory>>() {});

        bind(new TypeLiteral<Encoder<Metric, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<Metric>>() {});

        bind(new TypeLiteral<Encoder<MetricType, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<MetricType>>() {});

        bind(new TypeLiteral<Encoder<Category, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<Category>>() {});

        bind(new TypeLiteral<KeyProvider<RSA>>() {})
                .to(RSAKeyProvider.class);

        ///

        bind(new TypeLiteral<Serialiser<Inventory, byte[]>>() {})
                .to(InventorySerialiser.class);

        bind(new TypeLiteral<Serialiser<Inventory, String>>() {})
                .to(InventoryStringSerialiser.class);

        bind(new TypeLiteral<Serialiser<Category, byte[]>>() {})
                .to(new TypeLiteral<CategorySerialiser>() {});

        bind(new TypeLiteral<Serialiser<Category, String>>() {})
                .to(new TypeLiteral<CategoryStringSerialiser>() {});

        bind(new TypeLiteral<Serialiser<User, byte[]>>() {})
                .to(UserSerialiser.class);

        bind(new TypeLiteral<Serialiser<Metric, byte[]>>() {})
                .to(MetricSerialiser.class);

        bind(new TypeLiteral<Serialiser<Metric, String>>() {})
                .to(MetricStringSerialiser.class);

        bind(new TypeLiteral<Serialiser<MetricType, byte[]>>() {})
                .to(new TypeLiteral<MetricTypeSerialiser>() {});

        bind(new TypeLiteral<Serialiser<MetricType, String>>() {})
                .to(new TypeLiteral<MetricTypeStringSerialiser>() {});

        bind(new TypeLiteral<Responder<Inventory>>() {})
                .to(new TypeLiteral<JSONResponder<Inventory>>() {});

        ///

        bind(new TypeLiteral<UrlBuilder<Inventory>>() {})
                .to(InventoryUrlBuilder.class);

        ///
        bind(new TypeLiteral<IdExtractor<Long, Inventory>>() {})
                .to(InventoryIdExtractor.class);

        bind(new TypeLiteral<Saver<User>>() {})
                .to(new TypeLiteral<UserSaver<RSA>>() {});

        bind(new TypeLiteral<Saver<Inventory>>() {})
                .to(InventorySaver.class);

        bind(new TypeLiteral<Saver<Metric>>() {})
                .to(MetricSaver.class);

        bind(new TypeLiteral<Saver<MetricType>>() {})
                .to(new TypeLiteral<ChildFreeSaver<MetricType>>() {});

        bind(new TypeLiteral<Predicate<Inventory>>() {})
                .to(InventoryCategoryPredicate.class);

        bind(new TypeLiteral<Validator<User>>() {})
                .to(UserValidator.class);

        bind(new TypeLiteral<Validator<Inventory>>() {})
                .to(InventoryValidator.class);
    }

    @Provides
    @Singleton
    @Exposed
    @javax.inject.Named("inventory_root")
    public Pestlet<Inventory> providePestletInventory(@javax.inject.Named("current_inventory") com.google.inject.Provider<Inventory> provider,
                                                      com.google.inject.Provider<Responder<Inventory>> responder,
                                                      com.google.inject.Provider<Responder<Collection<Inventory>>> collectionResponder,
                                                      com.google.inject.Provider<DAO<Inventory>> dao,
                                                      com.google.inject.Provider<UrlBuilder<Inventory>> urlBuilder,
                                                      com.google.inject.Provider<Repository<Inventory, ?>> repository,
                                                      Validator<Inventory> validator) {
        return new Pestlet<>(provider, responder, collectionResponder, dao, urlBuilder, repository, validator);
    }

    @Provides
    @Singleton
    public TFilter<Long, Inventory, EntityManager> providesInventoryFromIdFilter(com.google.inject.Provider<Parser<Inventory>> parser,
                                                                                 com.google.inject.Provider<IdExtractor<Long, Inventory>> idExtractor,
                                                                                 com.google.inject.Provider<Repository<Inventory, EntityManager>> repository,
                                                                                 com.google.inject.Provider<FinderFactory<Inventory, Long, EntityManager>> finderFactory) {
        return new TFilter<>(parser, idExtractor, finderFactory, repository, Inventory.class, "inventory");
    }

    @Provides
    Responder<Collection<Inventory>> inventoryCollectionResponderProvider(Serialiser<Inventory, String> serialiser) {
        return new JSONListResponder<>(serialiser, "inventories");
    }
}