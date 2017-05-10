package com.tailoredshapes.inventoryserver;

import com.tailoredshapes.inventoryserver.dao.CategorySaver;
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
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateLookers;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserDAOTest {

  private User user;

  private EntityManager em;
  private Repository.FindBy<Category, EntityManager> categoryFindBy;
  private HibernateDAO<MetricType> metricTypeDAO;
  private HibernateDAO<Metric> metricDAO;
  private HibernateDAO<Category> categoryDAO;
  private HibernateDAO<Inventory> inventoryDAO;
  private HibernateDAO<User> userDAO;

  @Before
  public void setUp() throws Exception {
    em = TestPersistence.emf.createEntityManager();
    categoryFindBy = HibernateRepository.findBy(em);

    metricTypeDAO = new HibernateDAO<>(MetricType.class, em, (x, t) -> t, Encoders.shaEncoder);
    metricDAO = new HibernateDAO<>(Metric.class, em, new MetricSaver(metricTypeDAO), Encoders.shaEncoder);
    categoryDAO = new HibernateDAO<>(Category.class,
                                     em,
                                     new CategorySaver<>(categoryFindBy, HibernateLookers.catergoryByFullName),
                                     Encoders.shaEncoder);
    inventoryDAO =
      new HibernateDAO<>(Inventory.class, em, new InventorySaver(metricDAO, categoryDAO), Encoders.shaEncoder);
    userDAO = new HibernateDAO<>(User.class, em, new UserSaver(inventoryDAO), Encoders.shaEncoder);

    user = new User().setName("Archer");
  }


  @Test
  public void testHibernate() throws Exception {
    EntityTransaction transaction = em.getTransaction();
    transaction.begin();

    User savedUser = userDAO.create(user);

    assertNotNull(savedUser.getId());

    User readUser = userDAO.read(new User().setId(savedUser.getId()));
    assertEquals(savedUser, readUser);
    transaction.rollback();
  }

}
