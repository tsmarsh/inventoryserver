package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.extractors.InventoryIdExtractor;
import com.tailoredshapes.inventoryserver.extractors.UrlIdExtractor;
import com.tailoredshapes.inventoryserver.filters.ParameterFilter;
import com.tailoredshapes.inventoryserver.handlers.InventoryHandler;
import com.tailoredshapes.inventoryserver.handlers.UserHandler;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.security.RSA;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;

import javax.inject.Named;
import java.io.IOException;
import java.net.InetSocketAddress;

public class InventoryServerModule implements Module {
    private final String host;
    private final int port;

    public InventoryServerModule(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(HttpHandler.class)
                .to(InventoryHandler.class);

        binder.bind(new TypeLiteral<Parser<Inventory>>() {})
                .to(InventoryParser.class);

        binder.bind(new TypeLiteral<IdExtractor<User>>() {})
                .to(UrlIdExtractor.class);

        binder.bind(new TypeLiteral<IdExtractor<Inventory>>() {})
                .to(InventoryIdExtractor.class);

        binder.bind(new TypeLiteral<Saver<User>>() {})
                .to(new TypeLiteral<UserSaver<RSA>>() {});

        binder.bind(new TypeLiteral<Saver<Inventory>>() {})
                .to(InventorySaver.class);

        binder.bind(new TypeLiteral<Saver<Category>>() {})
                .to(new TypeLiteral<ChildFreeSaver<Category>>() {});

        binder.bind(new TypeLiteral<Saver<Metric>>() {})
                .to(MetricSaver.class);

        binder.bind(new TypeLiteral<Saver<MetricType>>() {})
                .to(new TypeLiteral<ChildFreeSaver<MetricType>>() {});
    }


    @Provides
    @Named("host")
    public String hostProvider() {
        return host;
    }

    @Provides
    @Named("port")
    public Integer portProvider() {
        return port;
    }

    @Provides
    @Named("protocol")
    public String protocolProvider() {
        return "http";
    }

    @Provides
    public UrlBuilder<User> userUrlBuilderProvider(@Named("protocol") String protocol,
                                                   @Named("host") String host,
                                                   @Named("port") Integer port) {
        return new UserUrlBuilder(protocol, host, port);
    }

    @Provides
    public UrlBuilder<Inventory> inventoryUrlBuilderProvider(@Named("protocol") String protocol,
                                                             @Named("host") String host,
                                                             @Named("port") Integer port) {
        return new InventoryUrlBuilder(protocol, host, port);
    }

    @Provides
    public HttpServer httpServerProvider(@Named("host") String host,
                                         @Named("port") Integer port,
                                         InventoryHandler handler,
                                         UserHandler userHander,
                                         ParameterFilter parameterFilter) {
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), -1);
            HttpContext inventoryContext = httpServer.createContext("/inventory", handler);
            inventoryContext.getFilters().add(parameterFilter);
            HttpContext userContext = httpServer.createContext("/user", userHander);
            userContext.getFilters().add(parameterFilter);
            return httpServer;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
