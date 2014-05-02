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

public class InventoryRootServletModule extends ServletModule {
    private final boolean usesPersistence;
    private final boolean usesTransactions;

    public InventoryRootServletModule(boolean usesPersistence, boolean usesTransactions) {
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

        serveRegex("/inventories/?(-?\\d+)?$").with(new Key<Pestlet<User>>() {});
        filterRegex("/inventories/-?\\d+$").through(new Key<TFilter<Long, Inventory, ?>>() {});
        filterRegex("/inventories").through(new Key<TFilter<Long, Inventory, ?>>() {});

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
}
