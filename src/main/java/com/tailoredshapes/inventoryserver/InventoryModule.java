package com.tailoredshapes.inventoryserver;

import com.google.inject.*;
import com.jolbox.bonecp.BoneCPConfig;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tailoredshapes.inventoryserver.filters.ParameterFilter;
import com.tailoredshapes.inventoryserver.handlers.InventoryHandler;
import com.tailoredshapes.inventoryserver.handlers.UserHandler;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.inject.Named;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * Created by tmarsh on 2/14/14.
 */
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
    public Configuration hibernateConfigurationProvider(@Named("hibernate") Properties properties){
        Configuration cfg = new Configuration();
        getClass().getResourceAsStream("hibernate.properties");
        cfg.setProperties(properties);
        cfg.addPackage("com.tailoredshapes.inventoryserver.model");
        return cfg;
    }

    @Provides
    @Singleton
    public SessionFactory sessionFactoryProvider(Configuration cfg){
        return cfg.buildSessionFactory();
    }


    @Provides
    @Named("host")
    public String hostProvider(){
        return host;
    }

    @Provides
    @Named("port")
    public Integer portProvider(){
        return port;
    }

    @Provides
    public HttpServer httpServerProvider(@Named("host") String host,
                                         @Named("port") Integer port,
                                         InventoryHandler handler,
                                         UserHandler userHander,
                                         ParameterFilter parameterFilter){
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), -1);
            HttpContext inventoryContext = httpServer.createContext("/inventory", handler);
            inventoryContext.getFilters().add(parameterFilter);
            HttpContext userContext = httpServer.createContext("/user", userHander);
            return httpServer;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
