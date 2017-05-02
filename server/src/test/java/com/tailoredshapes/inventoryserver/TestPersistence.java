package com.tailoredshapes.inventoryserver;

import java.util.HashMap;

import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManagerFactory;

import static com.tailoredshapes.inventoryserver.Persistence.archiverPersistenceUnitInfo;
import static com.tailoredshapes.inventoryserver.Persistence.managedClasses;
import static org.hibernate.cfg.AvailableSettings.DIALECT;
import static org.hibernate.cfg.AvailableSettings.GENERATE_STATISTICS;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.JPA_JDBC_DRIVER;
import static org.hibernate.cfg.AvailableSettings.JPA_JDBC_PASSWORD;
import static org.hibernate.cfg.AvailableSettings.JPA_JDBC_URL;
import static org.hibernate.cfg.AvailableSettings.JPA_JDBC_USER;
import static org.hibernate.cfg.AvailableSettings.QUERY_STARTUP_CHECKING;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;
import static org.hibernate.cfg.AvailableSettings.STATEMENT_BATCH_SIZE;
import static org.hibernate.cfg.AvailableSettings.USE_QUERY_CACHE;
import static org.hibernate.cfg.AvailableSettings.USE_REFLECTION_OPTIMIZER;
import static org.hibernate.cfg.AvailableSettings.USE_SECOND_LEVEL_CACHE;
import static org.hibernate.cfg.AvailableSettings.USE_STRUCTURED_CACHE;


public interface TestPersistence {
  String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
  String JDBC_URL = "jdbc:hsqldb:mem:testinventory;sql.syntax_ora=true";
  String JDBC_USER = "sa";
  String JDBC_PASSWORD = "";

  EntityManagerFactory emf = new HibernatePersistenceProvider().createContainerEntityManagerFactory(
    archiverPersistenceUnitInfo("TestPersistenceUnit", managedClasses),
    new HashMap<String, Object>() {{
      put(JPA_JDBC_DRIVER, JDBC_DRIVER);
      put(JPA_JDBC_URL, JDBC_URL);
      put(JPA_JDBC_USER, JDBC_USER);
      put(JPA_JDBC_PASSWORD, JDBC_PASSWORD);
      put(DIALECT, org.hibernate.dialect.HSQLDialect.class);
      put(HBM2DDL_AUTO, "create");
      put(SHOW_SQL, true);
      put(QUERY_STARTUP_CHECKING, true);
      put(GENERATE_STATISTICS, true);
      put(USE_REFLECTION_OPTIMIZER, true);
      put(USE_SECOND_LEVEL_CACHE, false);
      put(USE_QUERY_CACHE, false);
      put(USE_STRUCTURED_CACHE, false);
      put(STATEMENT_BATCH_SIZE, 20);
    }});
}

