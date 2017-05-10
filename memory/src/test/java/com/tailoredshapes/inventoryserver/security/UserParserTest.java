package com.tailoredshapes.inventoryserver.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.tailoredshapes.inventoryserver.dao.CategorySaver;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InventorySaver;
import com.tailoredshapes.inventoryserver.dao.MetricSaver;
import com.tailoredshapes.inventoryserver.dao.UserSaver;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.encoders.Encoders;
import com.tailoredshapes.inventoryserver.extractors.Extractors;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryLookers;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryRepository;
import com.tailoredshapes.inventoryserver.serialisers.InventoryStringSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.MetricStringSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.UserStringSerialiser;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class UserParserTest {

  private Parser<Inventory> inMemoryInventoryParser;
  private Repository.FindBy<Category, Map<Long, Category>> iMcategoryFindBy;
  private Repository.FindById<Inventory> iMinventoryFindById;
  private Repository.FindBy<MetricType, Map<Long, MetricType>> iMmetricTypeFindBy;
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

  private UserStringSerialiser userSerializer;

  @Before
  public void setUp() throws Exception {
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
    inMemoryInventoryParser = InventoryParser.inventoryParser(iMcategoryFindBy,
                                                              iMinventoryFindById,
                                                              iMmetricTypeFindBy, InMemoryLookers.categoryByFullName,
                                                              InMemoryLookers.metricTypeByName,
                                                              Extractors.inventoryExtractor);
    imUserParser = UserParser.userParser(iMuserFindById, inMemoryInventoryParser, Extractors.userIdExtractor);

    InventoryUrlBuilder inventoryUrlBuilder = new InventoryUrlBuilder("http", "localhost", 5555);
    userSerializer = new UserStringSerialiser(new UserUrlBuilder("http", "localhost", 5555),
                                              new InventoryStringSerialiser(inventoryUrlBuilder,
                                                                            new MetricStringSerialiser()));
  }

  @Test
  public void testParseNewUserInMemory() throws Exception {
    JSONObject userJSON = new JSONObject().put("name", "Archer");

    Parser<User> userParser = UserParser.userParser(InMemoryRepository.findById(new HashMap<>()),
                                                    inMemoryInventoryParser, Extractors.inventoryExtractor);

    User parsedUser = userParser.parse(userJSON.toString());
    assertEquals("Archer", parsedUser.getName());
  }


  @Test
  public void testParseExistingUserInMemory() throws Exception {
    Inventory inventory = new InventoryBuilder().id(null).build();

    Set<Inventory> inventories = new HashSet<>();
    inventories.add(inventory);

    User existingUser = new UserBuilder().id(null).name("Cassie").inventories(inventories).build();

    User savedUser = iMUserDAO.create(existingUser);
    savedUser.setName("Archer");

    String userJsonString = userSerializer.serialise(savedUser);

    User parsedUser = imUserParser.parse(userJsonString);

    assertEquals("Archer", parsedUser.getName());
    assertTrue(parsedUser.getInventories().iterator().next().getId().equals(inventory.getId()));
  }


  @Test
  public void testParseUpdatedUserInMemory() throws Exception {
    Runnable transactionCompleter = () -> {
    };
    Inventory inventory = new InventoryBuilder().id(null).build();
    Set<Inventory> inventories = new HashSet<>();
    inventories.add(inventory);

    User existingUser = new UserBuilder().id(null).name("Cassie").inventories(inventories).build();

    User savedUser = iMUserDAO.create(existingUser);

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

    User parsedUser = imUserParser.parse(userJsonString);

    assertEquals("Archer", parsedUser.getName());
    assertThat(parsedUser.getInventories(), hasItems(newInventory, inventory));
  }


}
