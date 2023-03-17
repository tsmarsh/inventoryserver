package com.tailoredshapes.inventoryserver;

import com.tailoredshapes.inventoryserver.api.client.DefaultApi;
import com.tailoredshapes.inventoryserver.api.model.Inventory;
import com.tailoredshapes.inventoryserver.api.model.Metric;
import com.tailoredshapes.inventoryserver.api.model.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static com.tailoredshapes.underbar.UnderBar.*;
import static org.junit.Assert.assertEquals;

public class InMemoryInventoryAPITest {

  @BeforeClass
  public static void start() {
    InMemoryServer.start();
  }

  @Before
  public void setUp() throws Exception {
    InMemoryServer.resetDB();
  }

  @Test
  public void shouldReturnAllInventories() throws Exception {
    DefaultApi api = new DefaultApi();
    List<Inventory> inventories = api.allInventories();
    assertEquals(0, inventories.size());

    Inventory inventory = new Inventory();

    inventory.setCategory("test.category");

    api.createInventory(inventory);

    List<Inventory> moreInventories = api.allInventories();
    assertEquals("test.category", first(moreInventories).getCategory());
  }

  @Test
  public void shouldReturnAllUsers() throws Exception {
    DefaultApi api = new DefaultApi();

    User user = new User();

    user.setName("Tilda");

    User saved = api.createUser (user);
    assertEquals("Tilda", saved.getName());

    List<User> users = api.allUsers();
    assertEquals("Tilda", first(users).getName());
  }

  @Test
  public void shouldAddAnInventoryOnAUser() throws Exception {
    DefaultApi api = new DefaultApi();

    Metric metric = new Metric();
    metric.setType("Arrows");
    metric.setValue("34");

    Inventory inventory = new Inventory();
    inventory.setMetrics(list(metric));
    inventory.setCategory("test.flarp");

    User user = new User();
    user.setName("Archer");

    Inventory archer = api.updateInventoryForUser(inventory,"Archer", "test.flarp");
    assertEquals("test.flarp", archer.getCategory());

    User savedArcher = api.findLatestUser("Archer");
    assertEquals("test.flarp", first(savedArcher.getInventories()).getCategory());
  }

  @Test
  public void shouldReturnAllInventoriesForAUser() throws Exception {
    DefaultApi api = new DefaultApi();

    Metric metric = new Metric();
    metric.setType("Arrows");
    metric.setValue("34");

    Inventory inventory = new Inventory();
    inventory.setMetrics(list(metric));
    inventory.setCategory("test.flarp");

    Metric metric2 = new Metric();
    metric2.setType("Bows");
    metric2.setValue("1");

    Inventory inventory2 = new Inventory();
    inventory2.setMetrics(list(metric2));
    inventory2.setCategory("test.floop");

    User user = new User();
    user.setName("Archer");

    api.createUser(user);

    Inventory inv1 = api.updateInventoryForUser(inventory, "Archer", "test.flarp");
    Inventory inv2 = api.updateInventoryForUser(inventory2, "Archer", "test.floop");

    List<Inventory> inventories = api.allInventoriesForUser("Archer");
    assertEquals(1, filter(inventories, (i) -> !i.getCategory().equals("test.flarp")).size());
    assertEquals(1, filter(inventories, (i) -> !i.getCategory().equals("test.floop")).size());
  }
}
