package com.tailoredshapes.inventoryserver.dao.memory;

import java.util.Map;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.Saver;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.Idable;

public class InMemoryDAO<T extends Idable<T>> implements DAO<T> {

  private final Map<Long, T> db;
  private final Encoder encoder;
  private final Saver<T> saver;

  public InMemoryDAO(Map<Long, T> db, Encoder encoder, Saver<T> saver) {
    this.encoder = encoder;
    this.saver = saver;
    this.db = db;
  }

  @Override
  public T create(T object) {
    object = saver.saveChildren(this, object);

    Long sig = encoder.encode(object);
    object.setId(sig);
    db.put(sig, object);
    return object;
  }

  @Override
  public T read(T object) {
    return db.get(object.getId());
  }

  @Override
  public T update(T object) {

    if (!db.containsKey(object.getId())) {
      throw new RuntimeException("Object does not exist");
    }
    object = saver.saveChildren(this, object);
    Long sig = encoder.encode(object);
    object.setId(sig);
    db.put(sig, object);
    return object;
  }

  @Override
  public T upsert(T object) {
    if (object != null) {
      if (null == object.getId()) {
        return create(object);
      } else {
        return update(object);
      }
    }
    return null;
  }
}

