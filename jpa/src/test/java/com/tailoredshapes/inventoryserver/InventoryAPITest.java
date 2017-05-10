package com.tailoredshapes.inventoryserver;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Inventory;
import io.swagger.client.model.Metric;
import io.swagger.client.model.User;

import static com.tailoredshapes.underbar.UnderBar.first;
import static com.tailoredshapes.underbar.UnderBar.list;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class InventoryAPITest {

  @BeforeClass
  public static void start() {

    Server.start();
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

    User saved = api.createUser(user);
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

    Inventory archer = api.updateInventoryForUser("Archer", "test.flarp", inventory);
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

    Inventory inv1 = api.updateInventoryForUser("Archer", "test.flarp", inventory);
    Inventory inv2 = api.updateInventoryForUser("Archer", "test.floop", inventory2);

    List<Inventory> inventories = api.allInventoriesForUser("Archer");
    assertTrue(inventories.contains(inv1));
    assertTrue(inventories.contains(inv2));
  }
}
