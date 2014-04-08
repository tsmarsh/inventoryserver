package com.tailoredshapes.inventoryserver;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.filters.TFilter;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.responders.Responder;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import com.tailoredshapes.inventoryserver.servlets.Pestlet;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class InventoryServletModule extends ServletModule {
    @Override
    protected void configureServlets() {
        install(new JpaPersistModule("inventory_server"));
        filter("/*").through(PersistFilter.class);

        serveRegex("/users/?-?\\d+/inventories(/-?\\d+)?").with(new Key<Pestlet<Inventory>>() {});
        filterRegex("/users/?-?\\d+/inventories(/-?\\d+)?").through(new Key<TFilter<User>>() {});
        filterRegex("/users/?-?\\d+/inventories(/-?\\d+)?").through(new Key<TFilter<Inventory>>() {});

        serveRegex("/users(/-?\\d+)?$").with(new Key<Pestlet<User>>() {});
        filterRegex("/users(/-?\\d+)?$").through(new Key<TFilter<User>>() {});

        serve("/*").with(new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                super.doGet(req, resp);
                resp.sendError(404, "Cannot serve: " + req.getPathInfo());

            }

            @Override
            protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                super.doPost(req, resp);
                resp.sendError(404, "Cannot serve: " + req.getPathInfo());
            }
        });
        bind(Key.get(User.class, Names.named("current_user"))).to(User.class).in(ServletScopes.REQUEST);
        bind(Key.get(Inventory.class, Names.named("current_inventory"))).to(Inventory.class).in(ServletScopes.REQUEST);
    }

    @Provides
    @Singleton
    public Pestlet<Inventory> providePestletInventory(@Named("current_inventory") Provider<Inventory> provider, Provider<Responder<Inventory>> responder, Provider<DAO<Inventory>> dao, Provider<UrlBuilder<Inventory>> urlBuilder){
        return new Pestlet<>(provider, responder, dao, urlBuilder);
    }

    @Provides
    @Singleton
    public Pestlet<User> providePestletUser(@Named("current_user") Provider<User> provider, Provider<Responder<User>> responder, Provider<DAO<User>> dao, Provider<UrlBuilder<User>> urlBuilder){
        return new Pestlet<>(provider, responder, dao, urlBuilder);
    }

    @Provides
    @Singleton
    public TFilter<User> providesUserFilter(Parser<User> parser, IdExtractor<User> idExtractor, Repository<User> repository){
        return new TFilter<>(parser, idExtractor, repository, User.class, "user");
    }

    @Provides
    @Singleton
    public TFilter<Inventory> providesInventoryFilter(Parser<Inventory> parser, IdExtractor<Inventory> idExtractor, Repository<Inventory> repository){
        return new TFilter<>(parser, idExtractor, repository, Inventory.class, "inventory");
    }
}
