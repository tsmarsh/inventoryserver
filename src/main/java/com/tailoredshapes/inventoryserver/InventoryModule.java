package com.tailoredshapes.inventoryserver;

import com.google.inject.*;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.filters.ParameterFilter;
import com.tailoredshapes.inventoryserver.handlers.*;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryCategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryInventoryRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryMetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryUserRepository;
import com.tailoredshapes.inventoryserver.utils.InventoryParser;
import com.tailoredshapes.inventoryserver.utils.Parser;
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
        binder.bind(HttpHandler.class).to(InventoryHandler.class);
        binder.bind(Encoder.class).to(RSAEncoder.class);
        binder.bind(new TypeLiteral<DAO<Inventory>>() {
        }).to(new TypeLiteral<InMemoryDAO<Inventory>>() {
        });
        binder.bind(UserDAO.class).to(InMemoryUserDAO.class);
        binder.bind(new TypeLiteral<DAO<Category>>() {
        }).to(new TypeLiteral<InMemoryDAO<Category>>() {
        });
        binder.bind(new TypeLiteral<DAO<Metric>>() {
        }).to(new TypeLiteral<InMemoryDAO<Metric>>() {
        });
        binder.bind(new TypeLiteral<DAO<MetricType>>() {
        }).to(new TypeLiteral<InMemoryDAO<MetricType>>() {
        });
        binder.bind(new TypeLiteral<Parser<Inventory>>() {
        }).to(InventoryParser.class);
        binder.bind(new TypeLiteral<Serialiser<Inventory>>() {
        }).to(new TypeLiteral<JSONSerialiser<Inventory>>() {
        });
        binder.bind(new TypeLiteral<Serialiser<Category>>() {
        }).to(new TypeLiteral<JSONSerialiser<Category>>() {
        });
        binder.bind(new TypeLiteral<Serialiser<User>>() {
        }).to(new TypeLiteral<JSONSerialiser<User>>() {
        });
        binder.bind(new TypeLiteral<Serialiser<Metric>>() {
        }).to(new TypeLiteral<JSONSerialiser<Metric>>() {
        });
        binder.bind(new TypeLiteral<Serialiser<MetricType>>() {
        }).to(new TypeLiteral<JSONSerialiser<MetricType>>() {
        });
        binder.bind(new TypeLiteral<Responder<Inventory>>() {
        }).to(new TypeLiteral<JSONResponder<Inventory>>() {
        });
        binder.bind(new TypeLiteral<Responder<User>>() {
        }).to(new TypeLiteral<JSONResponder<User>>() {
        });
        binder.bind(new TypeLiteral<InventoryRepository>() {
        }).to(InMemoryInventoryRepository.class);
        binder.bind(new TypeLiteral<CategoryRepository>() {
        }).to(InMemoryCategoryRepository.class);
        binder.bind(new TypeLiteral<UserRepository>() {
        }).to(InMemoryUserRepository.class);
        binder.bind(new TypeLiteral<MetricTypeRepository>() {
        }).to(InMemoryMetricTypeRepository.class);
        binder.bind(UserIdExtractor.class).to(UrlIdExtractor.class);
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
    public String protocolProvider(){
        return "http";
    }

    @Provides
    public UrlBuilder<User> userUrlBuilderProvider(@Named("protocol") String protocol,
                                                   @Named("host") String host,
                                                   @Named("port") Integer port){
        return new UserUrlBuilder(protocol, host, port);
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
