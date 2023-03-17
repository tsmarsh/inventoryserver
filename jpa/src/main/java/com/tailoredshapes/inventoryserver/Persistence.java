package com.tailoredshapes.inventoryserver;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.SharedCacheMode;
import jakarta.persistence.ValidationMode;
import jakarta.persistence.spi.ClassTransformer;
import jakarta.persistence.spi.PersistenceUnitInfo;
import jakarta.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import static com.tailoredshapes.underbar.UnderBar.list;
import static spark.Spark.halt;

public interface Persistence {

  List<String> managedClasses = list(
    Category.class.getCanonicalName(),
    Inventory.class.getCanonicalName(),
    Metric.class.getCanonicalName(),
    MetricType.class.getCanonicalName(),
    User.class.getCanonicalName()
  );

  static <T> T persistent(EntityManagerFactory emf, Function<EntityManager, T> f) {
    EntityManager em = emf.createEntityManager();
    return f.apply(em);
  }

  static <T> T transactional(EntityManagerFactory emf, Function<EntityManager, T> s) {
    return persistent(emf, (em) -> {
      EntityTransaction transaction = em.getTransaction();
      transaction.begin();
      T t;
      try {
        t = s.apply(em);
        em.flush();
        transaction.commit();
        return t;
      } catch (Exception e) {
        transaction.rollback();
        throw halt(e.getLocalizedMessage());
      }
    });
  }

  static PersistenceUnitInfo archiverPersistenceUnitInfo(final String name, final List<String> managedClasses) {
    return new PersistenceUnitInfo() {
      public String getPersistenceUnitName() {
        return name;
      }

      public String getPersistenceProviderClassName() {
        return "org.hibernate.jpa.HibernatePersistenceProvider";
      }

      public PersistenceUnitTransactionType getTransactionType() {
        return PersistenceUnitTransactionType.RESOURCE_LOCAL;
      }

      public DataSource getJtaDataSource() {
        return null;
      }

      public DataSource getNonJtaDataSource() {
        return null;
      }

      public List<String> getMappingFileNames() {
        return Collections.emptyList();
      }

      public List<URL> getJarFileUrls() {
        try {
          return Collections.list(this.getClass()
                                    .getClassLoader()
                                    .getResources(""));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }

      public URL getPersistenceUnitRootUrl() {
        return null;
      }

      public List<String> getManagedClassNames() {
        return managedClasses;
      }

      public boolean excludeUnlistedClasses() {
        return false;
      }

      public SharedCacheMode getSharedCacheMode() {
        return SharedCacheMode.ALL;
      }

      public ValidationMode getValidationMode() {
        return null;
      }

      public Properties getProperties() {
        return new Properties();
      }

      public String getPersistenceXMLSchemaVersion() {
        return null;
      }

      public ClassLoader getClassLoader() {
        return null;
      }

      public void addTransformer(ClassTransformer transformer) {}

      public ClassLoader getNewTempClassLoader() {
        return null;
      }
    };
  }
}
