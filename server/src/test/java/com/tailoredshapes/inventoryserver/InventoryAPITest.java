package com.tailoredshapes.inventoryserver;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Inventory;
import io.swagger.client.model.User;

import static com.tailoredshapes.underbar.UnderBar.first;
import static org.junit.Assert.assertEquals;

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

    api.createUser(user);

    List<User> users = api.allUsers();
    assertEquals("Tilda", first(users).getName());
  }

}
