package com.tailoredshapes.inventoryserver.dao.memory;

import java.util.HashMap;
import java.util.Map;

import com.tailoredshapes.inventoryserver.TestPersistence;
import com.tailoredshapes.inventoryserver.dao.CategorySaver;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InventorySaver;
import com.tailoredshapes.inventoryserver.dao.MetricSaver;
import com.tailoredshapes.inventoryserver.dao.UserSaver;
import com.tailoredshapes.inventoryserver.dao.hibernate.HibernateDAO;
import com.tailoredshapes.inventoryserver.encoders.Encoders;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateLookers;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryLookers;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryRepository;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDAOTest {

  private User user;

  private EntityManager em;
  private Parser<Inventory> inventoryParser;
  private Parser<Inventory> inMemoryInventoryParser;
  private Repository.FindBy<Category, Map<Long, Category>> iMcategoryFindBy;
  private Repository.FindById<Inventory> iMinventoryFindById;
  private Repository.FindBy<MetricType, Map<Long, MetricType>> iMmetricTypeFindBy;
  private Repository.FindBy<Category, EntityManager> categoryFindBy;
  private Repository.FindById<Inventory> inventoryFindById;
  private Repository.FindBy<MetricType, EntityManager> metricFindBy;
  private Map<Long, Category> catdb;
  private Map<Long, Inventory> invdb;
  private Map<Long, Metric> metdb;
  private Map<Long, MetricType> metTypedb;
  private Map<Long, User> udb;
  private DAO<Inventory> iMInventoryDAO;
  private DAO<User> iMUserDAO;
  private DAO<Metric> iMMetricDAO;
  private DAO<Category> iMCatDAO;
  private DAO<MetricType> iMMetricTypeDAO;
  private Repository.FindById<User> iMuserFindById;
  private Parser<User> imUserParser;
  private Repository.FindById<User> userFindById;
  private Parser<User> userParser;
  private HibernateDAO<MetricType> metricTypeDAO;
  private HibernateDAO<Metric> metricDAO;
  private HibernateDAO<Category> categoryDAO;
  private HibernateDAO<Inventory> inventoryDAO;
  private HibernateDAO<User> userDAO;

  @Before
  public void setUp() throws Exception {
    categoryFindBy = HibernateRepository.findBy(em);
    inventoryFindById = HibernateRepository.findById(Inventory.class, em);
    userFindById = HibernateRepository.findById(User.class, em);
    metricFindBy = HibernateRepository.findBy(em);

    metricTypeDAO = new HibernateDAO<>(MetricType.class, em, (x, t) -> t, Encoders.shaEncoder);
    metricDAO = new HibernateDAO<>(Metric.class, em, new MetricSaver(metricTypeDAO), Encoders.shaEncoder);
    categoryDAO = new HibernateDAO<>(Category.class,
                                     em,
                                     new CategorySaver<>(categoryFindBy, HibernateLookers.catergoryByFullName),
                                     Encoders.shaEncoder);
    inventoryDAO =
      new HibernateDAO<>(Inventory.class, em, new InventorySaver(metricDAO, categoryDAO), Encoders.shaEncoder);
    userDAO = new HibernateDAO<>(User.class, em, new UserSaver(inventoryDAO), Encoders.shaEncoder);

    catdb = new HashMap<>();
    invdb = new HashMap<>();
    metdb = new HashMap<>();
    metTypedb = new HashMap<>();
    udb = new HashMap<>();

    iMcategoryFindBy = InMemoryRepository.findBy(catdb);
    iMinventoryFindById = InMemoryRepository.findById(invdb);
    iMuserFindById = InMemoryRepository.findById(udb);
    iMmetricTypeFindBy = InMemoryRepository.findBy(metTypedb);

    iMMetricTypeDAO = new InMemoryDAO<>(metTypedb, Encoders.shaEncoder, (x, t) -> t);
    iMMetricDAO = new InMemoryDAO<>(metdb, Encoders.shaEncoder, new MetricSaver(iMMetricTypeDAO));
    iMCatDAO = new InMemoryDAO<>(catdb,
                                 Encoders.shaEncoder,
                                 new CategorySaver<>(iMcategoryFindBy, InMemoryLookers.categoryByFullName));
    iMInventoryDAO = new InMemoryDAO<>(invdb, Encoders.shaEncoder, new InventorySaver(iMMetricDAO, iMCatDAO));
    iMUserDAO = new InMemoryDAO<>(udb, Encoders.shaEncoder, new UserSaver(iMInventoryDAO));

    user = new User().setName("Archer");
  }

  @Test
  public void testInMemory() throws Exception {
    testSaveChildren(iMUserDAO);
  }

  @Test
  public void testHibernate() throws Exception {

    EntityManager em = TestPersistence.emf.createEntityManager();
    EntityTransaction transaction = em.getTransaction();
    transaction.begin();

    testSaveChildren(userDAO);
    transaction.rollback();
  }

  private void testSaveChildren(DAO<User> dao) throws Exception {
    User savedUser = dao.create(user);

    assertNotNull(savedUser.getId());

    User readUser = dao.read(new User().setId(savedUser.getId()));
    assertEquals(savedUser, readUser);
  }
}
