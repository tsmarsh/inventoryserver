package com.tailoredshapes.inventoryserver;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.filters.TFilter;
import com.tailoredshapes.inventoryserver.filters.TransactionFilter;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.responders.Responder;
import com.tailoredshapes.inventoryserver.servlets.Pestlet;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.validators.Validator;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collection;

public class UserRootServletModule extends ServletModule {
    private final boolean usesPersistence;
    private final boolean usesTransactions;

    public UserRootServletModule(boolean usesPersistence, boolean usesTransactions) {
        this.usesPersistence = usesPersistence;
        this.usesTransactions = usesTransactions;
    }

    @Override
    protected void configureServlets() {
        if (usesPersistence) {
            filter("/*").through(PersistFilter.class);
        }
        if (usesTransactions) {
            filter("/*").through(TransactionFilter.class);
        }

        serveRegex("/users/\\w+/?-?\\d+/inventories(/-?\\d+)?").with(new Key<Pestlet<Inventory>>() {});
        filterRegex("/users/\\w+/?-?\\d+/inventories(/-?\\d+)?").through(new Key<TFilter<Long, User, ?>>() {});
        filterRegex("/users/\\w+/?-?\\d+/inventories(/-?\\d+)?").through(new Key<TFilter<Long, Inventory, ?>>() {});

        serveRegex("/users/?(\\w+)?/?(-?\\d+)?$").with(new Key<Pestlet<User>>() {});
        filterRegex("/users/\\w+/-?\\d+$").through(new Key<TFilter<Long, User, ?>>() {});
        filterRegex("/users/\\w+$").through(new Key<TFilter<String, User, ?>>() {});
        filterRegex("/users$").through(new Key<TFilter<Long, User, ?>>() {});

        bind(Key.get(User.class, Names.named("current_user"))).to(User.class).in(ServletScopes.REQUEST);
        bind(Key.get(Inventory.class, Names.named("current_inventory"))).to(Inventory.class).in(ServletScopes.REQUEST);
    }

    @Provides
    @Singleton
    public Pestlet<Inventory> providePestletInventory(@Named("current_inventory") Provider<Inventory> provider,
                                                      Provider<Responder<Inventory>> responder,
                                                      Provider<Responder<Collection<Inventory>>> collectionResponder,
                                                      Provider<DAO<Inventory>> dao,
                                                      Provider<UrlBuilder<Inventory>> urlBuilder,
                                                      Provider<Repository<Inventory, ?>> repository,
                                                      Validator<Inventory> validator) {
        return new Pestlet<>(provider, responder, collectionResponder, dao, urlBuilder, repository, validator);
    }

    @Provides
    @Singleton
    public Pestlet<User> providePestletUser(@Named("current_user") Provider<User> provider,
                                            Provider<Responder<User>> responder,
                                            Provider<DAO<User>> dao,
                                            Provider<UrlBuilder<User>> urlBuilder,
                                            Provider<Responder<Collection<User>>> collectionResponder,
                                            Provider<Repository<User, ?>> repository, Validator<User> validator) {
        return new Pestlet<>(provider, responder, collectionResponder, dao, urlBuilder, repository, validator);
    }
}
