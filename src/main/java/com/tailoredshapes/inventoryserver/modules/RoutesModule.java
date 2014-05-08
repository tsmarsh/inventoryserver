package com.tailoredshapes.inventoryserver.modules;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.google.inject.persist.PersistFilter;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;
import com.tailoredshapes.inventoryserver.filters.TFilter;
import com.tailoredshapes.inventoryserver.filters.TransactionFilter;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.servlets.Pestlet;

public class RoutesModule extends ServletModule {
    private final boolean usesPersistence;
    private final boolean usesTransactions;

    public RoutesModule(boolean usesPersistence, boolean usesTransactions) {
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

        serve("/inventories*")
                .with(Key.get(new TypeLiteral<Pestlet<Inventory>>() {}, Names.named("inventory_root")));
        filter("/inventories*")
                .through(Key.get(new TypeLiteral<TFilter<Long, Inventory, ?>>() {}, Names.named("inventory_root")));

        serveRegex("/users/\\w+/?-?\\d+/inventories(/-?\\d+)?")
                .with(Key.get(new TypeLiteral<Pestlet<Inventory>>() {}, Names.named("user_root")));
        filterRegex("/users/\\w+/?-?\\d+/inventories(/-?\\d+)?")
                .through(Key.get(new TypeLiteral<TFilter<Long, User, ?>>() {}, Names.named("user_root")));
        filterRegex("/users/\\w+/?-?\\d+/inventories(/-?\\d+)?")
                .through(Key.get(new TypeLiteral<TFilter<Long, Inventory, ?>>() {}, Names.named("user_root")));

        serveRegex("/users/?(\\w+)?/?(-?\\d+)?$")
                .with(Key.get(new TypeLiteral<Pestlet<User>>() {}, Names.named("user_root")));
        filterRegex("/users/\\w+/-?\\d+$")
                .through(Key.get(new TypeLiteral<TFilter<Long, User, ?>>() {}, Names.named("user_root")));
        filterRegex("/users/\\w+$")
                .through(Key.get(new TypeLiteral<TFilter<String, User, ?>>() {}, Names.named("user_root")));
        filterRegex("/users$")
                .through(Key.get(new TypeLiteral<TFilter<Long, User, ?>>() {}, Names.named("user_root")));

        bind(Key.get(User.class, Names.named("current_user")))
                .to(User.class).in(ServletScopes.REQUEST);
        
        bind(Key.get(Inventory.class, Names.named("current_inventory")))
                .to(Inventory.class).in(ServletScopes.REQUEST);
    }
}