package com.tailoredshapes.inventoryserver;

import com.google.inject.*;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.hibernate.HibernateDAO;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateCategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateMetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateUserRepository;
import com.tailoredshapes.inventoryserver.security.RSA;
import com.tailoredshapes.inventoryserver.security.SHA;

import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateInventoryRepository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class HibernateModule implements Module{
    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<DAO<Inventory>>() {})
                .to(new TypeLiteral<HibernateDAO<Inventory, SHA>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<Inventory, SHA>>() {})
                .in(Singleton.class);


        binder.bind(new TypeLiteral<DAO<User>>() {})
                .to(new TypeLiteral<HibernateDAO<User, RSA>>() {});


        binder.bind(new TypeLiteral<HibernateDAO<User, RSA>>() {})
                .in(Singleton.class);


        binder.bind(new TypeLiteral<DAO<Category>>() {})
                .to(new TypeLiteral<HibernateDAO<Category, SHA>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<Category, SHA>>() {})
                .in(Singleton.class);


        binder.bind(new TypeLiteral<DAO<Metric>>() {})
                .to(new TypeLiteral<HibernateDAO<Metric, SHA>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<Metric, SHA>>() {})
                .in(Singleton.class);

        binder.bind(new TypeLiteral<DAO<MetricType>>() {})
                .to(new TypeLiteral<HibernateDAO<MetricType, SHA>>() {});

        binder.bind(new TypeLiteral<HibernateDAO<MetricType, SHA>>() {})
                .in(Singleton.class);


        binder.bind(InventoryRepository.class)
                .to(new TypeLiteral<HibernateInventoryRepository>() {});

        binder.bind(new TypeLiteral<CategoryRepository>() {})
                .to(new TypeLiteral<HibernateCategoryRepository>() {});

        binder.bind(new TypeLiteral<UserRepository>() {})
                .to(HibernateUserRepository.class);

        binder.bind(new TypeLiteral<MetricTypeRepository>() {})
                .to(new TypeLiteral<HibernateMetricTypeRepository>() {});
    }

    @Provides
    @Named("hibernate")
    public Properties getHibernateProperties() throws IOException {
        InputStream in = getClass().getResourceAsStream("/hibernate.properties");
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
        cfg.addResource("com/tailoredshapes/inventoryserver/model/User.hbm.xml");
        cfg.addResource("com/tailoredshapes/inventoryserver/model/Category.hbm.xml");
        cfg.addResource("com/tailoredshapes/inventoryserver/model/MetricType.hbm.xml");
        cfg.addResource("com/tailoredshapes/inventoryserver/model/Metric.hbm.xml");
        cfg.addResource("com/tailoredshapes/inventoryserver/model/Inventory.hbm.xml");
        return cfg;
    }

    @Provides
    @Singleton
    public SessionFactory sessionFactoryProvider(Configuration cfg) {
        return cfg.buildSessionFactory();
    }
}
