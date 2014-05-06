package com.tailoredshapes.inventoryserver.modules.jpa;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.dao.hibernate.HibernateDAO;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.encoders.RSAEncoder;
import com.tailoredshapes.inventoryserver.encoders.SHAEncoder;
import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.extractors.InventoryIdExtractor;
import com.tailoredshapes.inventoryserver.extractors.UserIdExtractor;
import com.tailoredshapes.inventoryserver.extractors.UserNameExtractor;
import com.tailoredshapes.inventoryserver.filters.TFilter;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;
import com.tailoredshapes.inventoryserver.repositories.InventoryCategoryPredicate;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.finders.categories.HibernateFindCategoryByFullName;
import com.tailoredshapes.inventoryserver.repositories.finders.inventories.HibernateFindInventoryById;
import com.tailoredshapes.inventoryserver.repositories.finders.metrictype.HibernateFindMetricTypeByName;
import com.tailoredshapes.inventoryserver.repositories.finders.users.HibernateFindUserById;
import com.tailoredshapes.inventoryserver.repositories.finders.users.HibernateFindUserByName;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateUserInventoryRepository;
import com.tailoredshapes.inventoryserver.responders.JSONListResponder;
import com.tailoredshapes.inventoryserver.responders.JSONResponder;
import com.tailoredshapes.inventoryserver.responders.Responder;
import com.tailoredshapes.inventoryserver.security.KeyProvider;
import com.tailoredshapes.inventoryserver.security.RSA;
import com.tailoredshapes.inventoryserver.security.RSAKeyProvider;
import com.tailoredshapes.inventoryserver.serialisers.*;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserRootInventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;
import com.tailoredshapes.inventoryserver.validators.InventoryValidator;
import com.tailoredshapes.inventoryserver.validators.UserValidator;
import com.tailoredshapes.inventoryserver.validators.Validator;

import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.Collection;
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


        ///

        binder.bind(new TypeLiteral<DAO<Inventory>>() {})
                .to(new TypeLiteral<HibernateDAO<Inventory>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<Inventory>>() {});


        binder.bind(new TypeLiteral<DAO<User>>() {})
                .to(new TypeLiteral<HibernateDAO<User>>() {});


        binder.bind(new TypeLiteral<HibernateDAO<User>>() {});


        binder.bind(new TypeLiteral<DAO<Category>>() {})
                .to(new TypeLiteral<HibernateDAO<Category>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<Category>>() {});


        binder.bind(new TypeLiteral<DAO<Metric>>() {})
                .to(new TypeLiteral<HibernateDAO<Metric>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<Metric>>() {});

        binder.bind(new TypeLiteral<DAO<MetricType>>() {})
                .to(new TypeLiteral<HibernateDAO<MetricType>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<MetricType>>() {});

        binder.bind(new TypeLiteral<FinderFactory<Category, String, EntityManager>>() {})
                .to(HibernateFindCategoryByFullName.class);

        binder.bind(new TypeLiteral<FinderFactory<MetricType, String, EntityManager>>() {})
                .to(HibernateFindMetricTypeByName.class);

        binder.bind(new TypeLiteral<FinderFactory<User, String, EntityManager>>() {})
                .to(HibernateFindUserByName.class);

        binder.bind(new TypeLiteral<FinderFactory<User, Long, EntityManager>>() {})
                .to(HibernateFindUserById.class);

        binder.bind(new TypeLiteral<FinderFactory<Inventory, Long, EntityManager>>() {})
                .to(HibernateFindInventoryById.class);

        binder.bind(new TypeLiteral<Parser<Inventory>>() {})
                .to(new TypeLiteral<InventoryParser<EntityManager, EntityManager>>() {});

        binder.bind(new TypeLiteral<Saver<Category>>() {})
                .to(new TypeLiteral<CategorySaver<EntityManager>>() {});


        binder.bind(new TypeLiteral<Repository<Inventory, ?>>() {})
                .to(new TypeLiteral<Repository<Inventory, EntityManager>>() {});

        binder.bind(new TypeLiteral<Repository<User, ?>>() {})
                .to(new TypeLiteral<Repository<User, EntityManager>>() {});

        binder.bind(new TypeLiteral<Repository<Category, ?>>() {})
                .to(new TypeLiteral<Repository<Category, EntityManager>>() {});

        binder.bind(new TypeLiteral<Repository<Metric, ?>>() {})
                .to(new TypeLiteral<Repository<Metric, EntityManager>>() {});

        binder.bind(new TypeLiteral<Repository<MetricType, ?>>() {})
                .to(new TypeLiteral<Repository<MetricType, EntityManager>>() {});

        binder.bind(new TypeLiteral<TFilter<Long, User, ?>>() {})
                .to(new TypeLiteral<TFilter<Long, User, EntityManager>>() {});

        binder.bind(new TypeLiteral<TFilter<String, User, ?>>() {})
                .to(new TypeLiteral<TFilter<String, User, EntityManager>>() {});

        binder.bind(new TypeLiteral<TFilter<Long, Inventory, ?>>() {})
                .to(new TypeLiteral<TFilter<Long, Inventory, EntityManager>>() {});

        binder.bind(new TypeLiteral<Encoder<User, ?>>() {})
                .to(new TypeLiteral<RSAEncoder<User>>() {});

        binder.bind(new TypeLiteral<Encoder<Inventory, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<Inventory>>() {});

        binder.bind(new TypeLiteral<Encoder<Metric, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<Metric>>() {});

        binder.bind(new TypeLiteral<Encoder<MetricType, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<MetricType>>() {});

        binder.bind(new TypeLiteral<Encoder<Category, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<Category>>() {});

        binder.bind(new TypeLiteral<KeyProvider<RSA>>() {})
                .to(RSAKeyProvider.class);

        ///

        binder.bind(new TypeLiteral<Serialiser<Inventory, byte[]>>() {})
                .to(InventorySerialiser.class);

        binder.bind(new TypeLiteral<Serialiser<Inventory, String>>() {})
                .to(InventoryStringSerialiser.class);

        binder.bind(new TypeLiteral<Serialiser<Category, byte[]>>() {})
                .to(new TypeLiteral<CategorySerialiser>() {});

        binder.bind(new TypeLiteral<Serialiser<Category, String>>() {})
                .to(new TypeLiteral<CategoryStringSerialiser>() {});

        binder.bind(new TypeLiteral<Serialiser<User, byte[]>>() {})
                .to(UserSerialiser.class);

        binder.bind(new TypeLiteral<Serialiser<User, String>>() {})
                .to(UserStringSerialiser.class);

        binder.bind(new TypeLiteral<Serialiser<Metric, byte[]>>() {})
                .to(MetricSerialiser.class);

        binder.bind(new TypeLiteral<Serialiser<Metric, String>>() {})
                .to(MetricStringSerialiser.class);

        binder.bind(new TypeLiteral<Serialiser<MetricType, byte[]>>() {})
                .to(new TypeLiteral<MetricTypeSerialiser>() {});

        binder.bind(new TypeLiteral<Serialiser<MetricType, String>>() {})
                .to(new TypeLiteral<MetricTypeStringSerialiser>() {});

        binder.bind(new TypeLiteral<Responder<Inventory>>() {})
                .to(new TypeLiteral<JSONResponder<Inventory>>() {});

        binder.bind(new TypeLiteral<Responder<User>>() {})
                .to(new TypeLiteral<JSONResponder<User>>() {});

        ///

        binder.bind(new TypeLiteral<UrlBuilder<User>>() {})
                .to(UserUrlBuilder.class);

        binder.bind(new TypeLiteral<UrlBuilder<Inventory>>() {})
                .to(UserRootInventoryUrlBuilder.class);

        ///

        binder.bind(new TypeLiteral<Parser<User>>() {})
                .to(UserParser.class);

        binder.bind(new TypeLiteral<IdExtractor<Long, User>>() {})
                .to(UserIdExtractor.class);

        binder.bind(new TypeLiteral<IdExtractor<String, User>>() {})
                .to(UserNameExtractor.class);

        binder.bind(new TypeLiteral<IdExtractor<Long, Inventory>>() {})
                .to(InventoryIdExtractor.class);

        binder.bind(new TypeLiteral<Saver<User>>() {})
                .to(new TypeLiteral<UserSaver<RSA>>() {});

        binder.bind(new TypeLiteral<Saver<Inventory>>() {})
                .to(InventorySaver.class);

        binder.bind(new TypeLiteral<Saver<Metric>>() {})
                .to(MetricSaver.class);

        binder.bind(new TypeLiteral<Saver<MetricType>>() {})
                .to(new TypeLiteral<ChildFreeSaver<MetricType>>() {});

        binder.bind(new TypeLiteral<Predicate<Inventory>>() {})
                .to(InventoryCategoryPredicate.class);

        binder.bind(new TypeLiteral<Validator<User>>() {})
                .to(UserValidator.class);

        binder.bind(new TypeLiteral<Validator<Inventory>>() {})
                .to(InventoryValidator.class);

    }

    @Provides
    public Repository<Inventory, EntityManager> inventoryRepositoryProvider(EntityManager manager,
                                                                            TypeLiteral<Inventory> type,
                                                                            @Named("current_user") Provider<User> parent,
                                                                            DAO<Inventory> dao, Repository<User, ?> parentRepo,
                                                                            Predicate<Inventory> filter) {
        return new HibernateUserInventoryRepository(manager, type, parent, dao, parentRepo, filter);
    }

    @Provides
    @Singleton
    public TFilter<Long, User, EntityManager> providesUserFromIdFilter(com.google.inject.Provider<Parser<User>> parser,
                                                                       com.google.inject.Provider<IdExtractor<Long, User>> idExtractor,
                                                                       com.google.inject.Provider<Repository<User, EntityManager>> repository,
                                                                       com.google.inject.Provider<FinderFactory<User, Long, EntityManager>> finderFactory) {
        return new TFilter<>(parser, idExtractor, finderFactory, repository, User.class, "user");
    }

    @Provides
    @Singleton
    public TFilter<String, User, EntityManager> providesUserFromNameFilter(com.google.inject.Provider<Parser<User>> parser,
                                                                           com.google.inject.Provider<IdExtractor<String, User>> idExtractor,
                                                                           com.google.inject.Provider<Repository<User, EntityManager>> repository,
                                                                           com.google.inject.Provider<FinderFactory<User, String, EntityManager>> finderFactory) {
        return new TFilter<>(parser, idExtractor, finderFactory, repository, User.class, "user");
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
    Responder<Collection<User>> userCollectionResponderProvider(Serialiser<User, String> serialiser) {
        return new JSONListResponder<>(serialiser, "users");
    }

    @Provides
    Responder<Collection<Inventory>> inventoryCollectionResponderProvider(Serialiser<Inventory, String> serialiser) {
        return new JSONListResponder<>(serialiser, "inventories");
    }
}
