package com.tailoredshapes.inventoryserver;

import com.google.inject.*;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.dao.memory.*;
import com.tailoredshapes.inventoryserver.encoders.*;
import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.extractors.InventoryIdExtractor;
import com.tailoredshapes.inventoryserver.extractors.UrlIdExtractor;
import com.tailoredshapes.inventoryserver.filters.ParameterFilter;
import com.tailoredshapes.inventoryserver.handlers.*;
import com.tailoredshapes.inventoryserver.handlers.responders.JSONResponder;
import com.tailoredshapes.inventoryserver.handlers.responders.Responder;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryCategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryInventoryRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryMetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryUserRepository;
import com.tailoredshapes.inventoryserver.serialisers.*;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;
import com.tailoredshapes.inventoryserver.security.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

public class InventoryModule implements Module {
    private final String host;
    private final int port;

    public InventoryModule(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(HttpHandler.class)
                .to(InventoryHandler.class);

        binder.bind(new TypeLiteral<DAO<Inventory>>() {})
                .to(new TypeLiteral<InMemoryDAO<Inventory, RSA>>() {});

        binder.bind(new TypeLiteral<InMemoryDAO<Inventory, RSA>>() {})
                .in(Singleton.class);


        binder.bind(new TypeLiteral<DAO<User>>() {})
                .to(new TypeLiteral<InMemoryDAO<User, RSA>>() {});


        binder.bind(new TypeLiteral<InMemoryDAO<User, RSA>>() {})
                .in(Singleton.class);


        binder.bind(new TypeLiteral<DAO<Category>>() {})
                .to(new TypeLiteral<InMemoryDAO<Category, SHA>>() {});

        binder.bind(new TypeLiteral<InMemoryDAO<Category, SHA>>() {})
                .in(Singleton.class);


        binder.bind(new TypeLiteral<DAO<Metric>>() {})
                .to(new TypeLiteral<InMemoryDAO<Metric, SHA>>() {});

        binder.bind(new TypeLiteral<InMemoryDAO<Metric, SHA>>() {})
                .in(Singleton.class);

        binder.bind(new TypeLiteral<DAO<MetricType>>() {})
                .to(new TypeLiteral<InMemoryDAO<MetricType, SHA>>() {});

        binder.bind(new TypeLiteral<InMemoryDAO<MetricType, SHA>>() {})
                .in(Singleton.class);

        binder.bind(new TypeLiteral<Parser<Inventory>>() {})
                .to(InventoryParser.class);

        binder.bind(new TypeLiteral<Serialiser<Inventory>>() {})
                .to(InventorySerialiser.class);

        binder.bind(new TypeLiteral<Serialiser<Category>>() {})
                .to(new TypeLiteral<JSONSerialiser<Category>>() {});

        binder.bind(new TypeLiteral<Serialiser<User>>() {})
                .to(UserSerialiser.class);

        binder.bind(new TypeLiteral<Serialiser<Metric>>() {})
                .to(MetricSeriliser.class);

        binder.bind(new TypeLiteral<Serialiser<MetricType>>() {})
                .to(new TypeLiteral<JSONSerialiser<MetricType>>() {});

        binder.bind(new TypeLiteral<Responder<Inventory>>() {})
                .to(new TypeLiteral<JSONResponder<Inventory>>() {});

        binder.bind(new TypeLiteral<Responder<User>>() {})
                .to(new TypeLiteral<JSONResponder<User>>() {});

        binder.bind(InventoryRepository.class)
                .to(new TypeLiteral<InMemoryInventoryRepository<RSA>>() {});

        binder.bind(new TypeLiteral<CategoryRepository>() {})
                .to(new TypeLiteral<InMemoryCategoryRepository<SHA>>() {});

        binder.bind(new TypeLiteral<UserRepository>() {})
                .to(InMemoryUserRepository.class);

        binder.bind(new TypeLiteral<MetricTypeRepository>() {})
                .to(new TypeLiteral<InMemoryMetricTypeRepository<SHA>>() {});

        binder.bind(new TypeLiteral<IdExtractor<User>>() {})
                .to(UrlIdExtractor.class);

        binder.bind(new TypeLiteral<IdExtractor<Inventory>>() {})
                .to(InventoryIdExtractor.class);

        binder.bind(new TypeLiteral<Encoder<User, RSA>>() {})
                .to(new TypeLiteral<RSAEncoder<User>>() {});

        binder.bind(new TypeLiteral<Encoder<Inventory, RSA>>() {})
                .to(new TypeLiteral<RSAEncoder<Inventory>>() {});

        binder.bind(new TypeLiteral<Encoder<Metric, SHA>>() {})
                .to(new TypeLiteral<SHAEncoder<Metric>>() {});

        binder.bind(new TypeLiteral<Encoder<MetricType, SHA>>() {})
                .to(new TypeLiteral<SHAEncoder<MetricType>>() {});

        binder.bind(new TypeLiteral<Encoder<Category, SHA>>() {})
                .to(new TypeLiteral<SHAEncoder<Category>>() {});

        binder.bind(new TypeLiteral<KeyProvider<RSA>>() {})
                .to(RSAKeyProvider.class);

        binder.bind(new TypeLiteral<Saver<User>>(){})
                .to(new TypeLiteral<UserSaver<RSA>>() {});

        binder.bind(new TypeLiteral<Saver<Inventory>>(){})
                .to(InventorySaver.class);

        binder.bind(new TypeLiteral<Saver<Category>>(){})
                .to(new TypeLiteral<ChildFreeSaver<Category>>() {});

        binder.bind(new TypeLiteral<Saver<Metric>>(){})
                .to(MetricSaver.class);

        binder.bind(new TypeLiteral<Saver<MetricType>>(){})
                .to(new TypeLiteral<ChildFreeSaver<MetricType>>(){});
    }

    @Provides
    @Named("hibernate")
    public Properties getHibernateProperties() throws IOException {
        InputStream in = getClass().getResourceAsStream("hibernate.properties");
        Properties properties = new Properties();
        properties.load(in);
        return properties;
    }

    @Provides
    @Singleton
    public Configuration hibernateConfigurationProvider(@Named("hibernate") Properties properties) {
        Configuration cfg = new Configuration();
        getClass().getResourceAsStream("hibernate.properties");
        cfg.setProperties(properties);
        cfg.addPackage("com.tailoredshapes.inventoryserver.model");
        return cfg;
    }

    @Provides
    @Singleton
    public SessionFactory sessionFactoryProvider(Configuration cfg) {
        return cfg.buildSessionFactory();
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
