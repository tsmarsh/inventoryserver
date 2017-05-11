package com.tailoredshapes.inventoryserver.memory;

import java.util.HashMap;
import java.util.Map;

import com.tailoredshapes.inventoryserver.dao.CategorySaver;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InventorySaver;
import com.tailoredshapes.inventoryserver.dao.MetricSaver;
import com.tailoredshapes.inventoryserver.dao.UserSaver;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.encoders.Encoders;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryLookers;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryRepository;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDAOTest {

  private User user;

  private Repository.FindBy<Category, Map<Long, Category>> iMcategoryFindBy;
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

  @Before
  public void setUp() throws Exception {
    catdb = new HashMap<>();
    invdb = new HashMap<>();
    metdb = new HashMap<>();
    metTypedb = new HashMap<>();
    udb = new HashMap<>();

    iMcategoryFindBy = InMemoryRepository.findBy(catdb);

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
    User savedUser = iMUserDAO.create(user);

    assertNotNull(savedUser.getId());

    User readUser = iMUserDAO.read(new User().setId(savedUser.getId()));
    assertEquals(savedUser, readUser);
  }

}
