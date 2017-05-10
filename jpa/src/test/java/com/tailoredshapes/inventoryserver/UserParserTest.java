package com.tailoredshapes.inventoryserver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tailoredshapes.inventoryserver.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.dao.CategorySaver;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InventorySaver;
import com.tailoredshapes.inventoryserver.dao.MetricSaver;
import com.tailoredshapes.inventoryserver.dao.UserSaver;
import com.tailoredshapes.inventoryserver.dao.hibernate.HibernateDAO;
import com.tailoredshapes.inventoryserver.encoders.Encoders;
import com.tailoredshapes.inventoryserver.extractors.Extractors;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateLookers;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;
import com.tailoredshapes.inventoryserver.serialisers.InventoryStringSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.MetricStringSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.UserStringSerialiser;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class UserParserTest {


  private EntityManager em;
  private Parser<Inventory> inventoryParser;
  private Repository.FindBy<Category, EntityManager> categoryFindBy;
  private Repository.FindById<Inventory> inventoryFindById;
  private Repository.FindBy<MetricType, EntityManager> metricFindBy;

  private Repository.FindById<User> userFindById;
  private Parser<User> userParser;
  private HibernateDAO<MetricType> metricTypeDAO;
  private HibernateDAO<Metric> metricDAO;
  private HibernateDAO<Category> categoryDAO;
  private HibernateDAO<Inventory> inventoryDAO;
  private HibernateDAO<User> userDAO;
  private UserStringSerialiser userSerializer;

  @Before
  public void setUp() throws Exception {
    em = TestPersistence.emf.createEntityManager();
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

    inventoryParser = InventoryParser.inventoryParser(categoryFindBy, inventoryFindById, metricFindBy,
                                                      HibernateLookers.catergoryByFullName,
                                                      HibernateLookers.metricTypeByName,
                                                      Extractors.inventoryExtractor);

    userParser = UserParser.userParser(userFindById, inventoryParser, Extractors.userIdExtractor);


    InventoryUrlBuilder inventoryUrlBuilder = new InventoryUrlBuilder("http", "localhost", 5555);
    userSerializer = new UserStringSerialiser(new UserUrlBuilder("http", "localhost", 5555),
                                              new InventoryStringSerialiser(inventoryUrlBuilder,
                                                                            new MetricStringSerialiser()));
  }


  @Test
  public void testParseNewUserHibernate() throws Exception {
    EntityTransaction transaction = em.getTransaction();

    transaction.begin();

    JSONObject userJSON = new JSONObject().put("name", "Archer");

    User parsedUser = userParser.parse(userJSON.toString());
    assertEquals("Archer", parsedUser.getName());

    transaction.rollback();
  }


  @Test
  public void testParseExistingUserHibernate() throws Exception {
    EntityTransaction transaction = em.getTransaction();

    transaction.begin();
    Inventory inventory = new InventoryBuilder().id(null).build();

    Set<Inventory> inventories = new HashSet<>();
    inventories.add(inventory);

    User existingUser = new UserBuilder().id(null).name("Cassie").inventories(inventories).build();

    User savedUser = userDAO.create(existingUser);
    savedUser.setName("Archer");

    String userJsonString = userSerializer.serialise(savedUser);

    User parsedUser = userParser.parse(userJsonString);

    assertEquals("Archer", parsedUser.getName());
    assertTrue(parsedUser.getInventories().iterator().next().getId().equals(inventory.getId()));
    transaction.rollback();
  }


  @Test
  public void testParseUpdatedUserHibernate() throws Exception {

    EntityTransaction transaction = em.getTransaction();

    transaction.begin();

    Runnable transactionCompleter = () -> em.flush();
    Inventory inventory = new InventoryBuilder().id(null).build();
    Set<Inventory> inventories = new HashSet<>();
    inventories.add(inventory);

    User existingUser = new UserBuilder().id(null).name("Cassie").inventories(inventories).build();

    User savedUser = userDAO.create(existingUser);

    transactionCompleter.run();

    User clone = savedUser.clone();

    Category fullname = new CategoryBuilder().name(null).fullname("another.test").build();

    Inventory newInventory = new InventoryBuilder().id(null).category(fullname).build();
    clone.setName("Archer");

    Set<Inventory> inventorySet = new HashSet<>();
    inventorySet.add(inventory);
    inventorySet.add(newInventory);

    clone.setInventories(inventorySet);

    assertFalse(savedUser.hashCode() == clone.hashCode());

    String userJsonString = userSerializer.serialise(clone);

    User parsedUser = userParser.parse(userJsonString);

    assertEquals("Archer", parsedUser.getName());
    assertThat(parsedUser.getInventories(), hasItems(newInventory, inventory));

    transaction.rollback();
  }

}
