package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
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
                .to(CategorySaver.class);

        binder.bind(new TypeLiteral<Saver<Metric>>() {})
                .to(MetricSaver.class);

        binder.bind(new TypeLiteral<Saver<MetricType>>() {})
                .to(new TypeLiteral<ChildFreeSaver<MetricType>>() {});

        binder.bind(new TypeLiteral<UrlBuilder<User>>(){})
                .to(UserUrlBuilder.class);

        binder.bind(new TypeLiteral<UrlBuilder<Inventory>>() {})
                .to(InventoryUrlBuilder.class);

        binder.bind(String.class)
                .annotatedWith(Names.named("host"))
                .toInstance(host);

        binder.bind(Integer.class)
                .annotatedWith(Names.named("port"))
                .toInstance(port);

        binder.bind(String.class)
                .annotatedWith(Names.named("protocol"))
                .toInstance("http");
    }
}
