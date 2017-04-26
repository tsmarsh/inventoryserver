package com.tailoredshapes.inventoryserver.repositories.hibernate;

import com.tailoredshapes.inventoryserver.TestPersistence;
import com.tailoredshapes.inventoryserver.dao.CategorySaver;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InventorySaver;
import com.tailoredshapes.inventoryserver.dao.MetricSaver;
import com.tailoredshapes.inventoryserver.dao.Saver;
import com.tailoredshapes.inventoryserver.dao.UserSaver;
import com.tailoredshapes.inventoryserver.dao.hibernate.HibernateDAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static com.tailoredshapes.inventoryserver.encoders.Encoders.shaEncoder;
import static com.tailoredshapes.underbar.UnderBar.list;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HibernateInventoryRepositoryTest {

  private HibernateDAO<Inventory> inventoryDAO;
  private EntityTransaction transaction;
  private EntityManager em;
  private User user;
  private HibernateDAO<User> userDAO;

  @Before
  public void setUp() throws Exception {
    em = TestPersistence.emf.createEntityManager();

    transaction = em.getTransaction();

    transaction.begin();
    Saver<MetricType> metricTypeSaver = (x, t) -> t;

    DAO<MetricType> metricTypeDAO = new HibernateDAO<>(MetricType.class, em, metricTypeSaver, shaEncoder);
    Saver<Metric> metricSaver = new MetricSaver(metricTypeDAO);

    DAO<Metric> metricDAO = new HibernateDAO<>(Metric.class, em, metricSaver, shaEncoder);
    Saver<Category> categorySaver =
      new CategorySaver(HibernateRepository.findBy(em), HibernateLookers.catergoryByFullName);
    DAO<Category> categoryDAO = new HibernateDAO<>(Category.class, em, categorySaver, shaEncoder);
    Saver<Inventory> saver = new InventorySaver(metricDAO, categoryDAO);

    inventoryDAO = new HibernateDAO<>(Inventory.class, em, saver, shaEncoder);

    user = new UserBuilder().build();
    Saver<User> userSaver = new UserSaver(inventoryDAO);
    userDAO = new HibernateDAO<>(User.class, em, userSaver, shaEncoder);
    userDAO.create(user);
  }

  @After
  public void tearDown() throws Exception {
    transaction.rollback();
  }

  @Test
  public void testFindById() throws Exception {
    Inventory inventory = new InventoryBuilder().build();

    Inventory savedInventory = inventoryDAO.create(inventory);
    em.flush();
    em.clear();
    Inventory byId = HibernateRepository.findById(Inventory.class, em).findById(savedInventory.getId());
    assertEquals(savedInventory, byId);

  }

  @Test
  public void testSave() throws Exception {
    Inventory inventory = new InventoryBuilder().id(null).build();

    user.setInventories(list(inventory));

    Repository.save(inventoryDAO).save(inventory);
    Repository.save(userDAO).save(user);
    em.flush();
    em.clear();

    User foundUser = HibernateRepository.findById(User.class, em).findById(user.getId());
    assertTrue(foundUser.getInventories().contains(inventory));
    assertNotNull(inventory.getId());

  }

  @Test
  public void shouldFindUserById() throws Exception {

    User storedUser = userDAO.create(new UserBuilder().build());

    User byId = HibernateRepository.findById(User.class, em).findById(storedUser.getId());
    Assert.assertEquals(storedUser, byId);

  }
}
