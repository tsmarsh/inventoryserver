package com.tailoredshapes.inventoryserver.dao.memory;

import java.util.HashMap;
import java.util.Map;

import com.tailoredshapes.inventoryserver.dao.ChildFreeSaver;
import com.tailoredshapes.inventoryserver.dao.Saver;
import com.tailoredshapes.inventoryserver.model.TestModel;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ChildFreeSaverTest {

  private TestModel model;

  private Saver<TestModel> saver = new ChildFreeSaver<>();

  @Test
  public void shouldUpdateAnObject() {
    Map<Long, TestModel> db = new HashMap<>();
    Long testId = 1L;

    InMemoryDAO<TestModel> dao = new InMemoryDAO<>(db, (x) -> 1L, saver);
    model = new TestModel().setValue("twifty");
    TestModel returnedTestModel = dao.create(model);

    assertEquals(testId, returnedTestModel.getId());
    assertEquals("twifty", returnedTestModel.getValue());

    TestModel updatedModel = new TestModel().setId(testId).setValue("eleventy");
    dao.update(updatedModel);

    TestModel lookupModel = new TestModel().setId(testId);

    TestModel readModel = dao.read(lookupModel);
    assertEquals("eleventy", readModel.getValue());
  }
}
