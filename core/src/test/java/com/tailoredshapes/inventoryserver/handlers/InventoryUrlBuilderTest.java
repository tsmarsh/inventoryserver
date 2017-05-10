package com.tailoredshapes.inventoryserver.handlers;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserRootInventoryUrlBuilder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InventoryUrlBuilderTest {

  @Test
  public void testShouldReturnTheCorrectUrlForAnInventory() throws Exception {
    Inventory inventory = new InventoryBuilder()
      .id(141211l).build();

    User user = new UserBuilder().name("Cassie").id(51284L).build();
    UrlBuilder<Inventory> inventoryUrlBuilder = new UserRootInventoryUrlBuilder(user, "http", "localhost", 4444);
    String url = inventoryUrlBuilder.build(inventory);
    assertEquals("http://localhost:4444/users/Cassie/51284/inventories/141211", url);
  }

  @Test
  public void testShouldReturnNullIfIdNotSet() throws Exception {

    Inventory inventory = new InventoryBuilder()
      .id(null).build();
    UrlBuilder<Inventory> inventoryUrlBuilder = new InventoryUrlBuilder("http", "localhost", 4444);
    String url = inventoryUrlBuilder.build(inventory);
    assertNull(url);

  }
}
