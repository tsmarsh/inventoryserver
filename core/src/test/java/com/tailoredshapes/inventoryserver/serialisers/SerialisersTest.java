package com.tailoredshapes.inventoryserver.serialisers;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricTypeBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;

import org.junit.Test;

import static com.tailoredshapes.inventoryserver.serialisers.Serialisers.categorySerialiser;
import static com.tailoredshapes.inventoryserver.serialisers.Serialisers.inventorySerializerBuilder;
import static com.tailoredshapes.inventoryserver.serialisers.Serialisers.metricSerialiser;
import static com.tailoredshapes.inventoryserver.serialisers.Serialisers.userSerializerBuilder;
import static com.tailoredshapes.underbar.UnderBar.list;
import static org.junit.Assert.*;


public class SerialisersTest {
  @Test
  public void shouldSerialiseACategory() throws Exception {
    Category cat = new CategoryBuilder().id(1L).name("foo").fullname("foo").build();

    String catString = categorySerialiser.serialise(cat);

    assertEquals("{\"name\":\"foo\",\"id\":1,\"fullname\":\"foo\"}", catString);
  }

  @Test
  public void shouldSerialiseACategoryWithAParent() throws Exception {
    Category parent = new CategoryBuilder().id(3L).name("bar").fullname("bar").build();
    Category cat = new CategoryBuilder().id(1L).parent(parent).name("foo").fullname("bar.foo").build();

    assertEquals("{\"parent\":{\"name\":\"foo\",\"id\":1,\"fullname\":\"bar.foo\"},\"name\":\"foo\",\"id\":1,\"fullname\":\"bar.foo\"}",
                 categorySerialiser.serialise(cat));
  }

  @Test
  public void shouldSeriliseAMetric() throws Exception {
    Metric m =
      new MetricBuilder().id(1L).type(new MetricTypeBuilder().name("drag").build()).value("alaska").build();

    assertEquals("{\"id\":1,\"type\":\"drag\",\"value\":\"alaska\"}", metricSerialiser.serialise(m));
  }

  @Test
  public void shouldSerialiseAnInventory() throws Exception {
    Category cat = new CategoryBuilder().id(1L).name("foo").fullname("bar.foo").build();

    Metric m =
      new MetricBuilder().id(1L).type(new MetricTypeBuilder().name("drag").build()).value("alaska").build();

    Inventory i = new InventoryBuilder().category(cat).metrics(list(m)).parent(null).build();

    assertEquals(
      "{\"id\":\"fred\",\"metrics\":[{\"id\":1,\"type\":\"drag\",\"value\":\"alaska\"}],\"category\":\"bar.foo\"}",
      inventorySerializerBuilder.apply((inv)-> "fred", metricSerialiser).serialise(i));
  }

  @Test
  public void shouldSerialiserAUser() throws Exception {
    Category cat = new CategoryBuilder().id(1L).name("foo").fullname("bar.foo").build();

    Metric m =
      new MetricBuilder().id(1L).type(new MetricTypeBuilder().name("drag").build()).value("alaska").build();

    Inventory i = new InventoryBuilder().category(cat).metrics(list(m)).parent(null).build();

    User tilda = new UserBuilder().id(444L).inventories(list(i)).name("Tilda").build();

    assertEquals(
      "{\"inventories\":[{\"yarp\":\"narp\"}],\"name\":\"Tilda\",\"id\":\"url\"}",
      userSerializerBuilder.apply((u)-> "url", (inv) -> "{\"yarp\":\"narp\"}").serialise(tilda));
  }
}