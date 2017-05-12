package com.tailoredshapes.inventoryserver.repositories;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.model.TestModel;

import org.junit.Test;

import static com.tailoredshapes.underbar.Die.die;
import static org.junit.Assert.*;

public class RepositoryTest {
  @Test
  public void shouldCreateAnUnidentifiedObject() throws Exception {
    TestModel foo = new TestModel().setId(null).setValue("foo");

    Repository.Save<TestModel> saver = Repository.save(new DAO<TestModel>() {
      @Override
      public TestModel create(TestModel object) {
        return object.setId(1205L);
      }

      @Override
      public TestModel read(TestModel object) {
        return die("Should not happen");
      }

      @Override
      public TestModel update(TestModel object) {
        return die("Should not happen");
      }

      @Override
      public TestModel upsert(TestModel object) {
        return die("Should not happen");
      }
    });

    assertEquals(1205L, (long) saver.save(foo).getId());
  }

  @Test
  public void shouldUpdateAnIdentifiedObject() throws Exception {
    TestModel foo = new TestModel().setId(1010L).setValue("foo");

    Repository.Save<TestModel> saver = Repository.save(new DAO<TestModel>() {
      @Override
      public TestModel create(TestModel object) {
        return die("Should not happen");
      }

      @Override
      public TestModel read(TestModel object) {
        return die("Should not happen");
      }

      @Override
      public TestModel update(TestModel object) {
        return object.setId(1414L);
      }

      @Override
      public TestModel upsert(TestModel object) {
        return die("Should not happen");
      }
    });

    assertEquals(1414L, (long) saver.save(foo).getId());
  }
}