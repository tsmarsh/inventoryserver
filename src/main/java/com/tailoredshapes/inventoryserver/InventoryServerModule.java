package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.extractors.InventoryIdExtractor;
import com.tailoredshapes.inventoryserver.extractors.UrlIdExtractor;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.security.RSA;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;

import javax.inject.Named;

public class InventoryServerModule implements Module {
    private final String host;
    private final int port;

    public InventoryServerModule(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void configure(Binder binder) {

        binder.bind(new TypeLiteral<Parser<User>>(){})
                .to(UserParser.class);

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

        binder.bind(new TypeLiteral<UrlBuilder<User>>(){})
                .to(UserUrlBuilder.class);

        binder.bind(new TypeLiteral<UrlBuilder<Inventory>>(){})
                .to(InventoryUrlBuilder.class);
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
}
