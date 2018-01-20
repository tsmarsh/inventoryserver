package com.tailoredshapes.inventoryserver.dao.hibernate;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.Saver;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.model.ShallowCopy;

import javax.persistence.EntityManager;

@SuppressWarnings("unchecked")
public class HibernateDAO<T extends Cloneable & Idable<T> & ShallowCopy<T>> implements DAO<T> {
  private final Class<? super T> rawType;
  private final EntityManager manager;
  private final Saver<T> saver;
  private final Encoder encoder;


  public HibernateDAO(Class<T> type, EntityManager manager, Saver<T> saver, Encoder encoder) {
    this.manager = manager;
    this.saver = saver;
    this.encoder = encoder;
    rawType = type;
  }

  @Override
  public T create(T object) {
    object = saver.saveChildren(this, object);

    Long sig = encoder.encode(object);

    object.setId(sig);
    T read = read(object);
    T out;
    if (read == null) {
      manager.persist(object);
      out = object;
    } else {
      out = read;
    }

    return out;
  }

  @Override
  public T read(T object) {
    return (T) manager.find(rawType, object.getId());
  }

  @Override
  public T update(T object) {
    T clone = object.shallowCopy();
    clone = saver.saveChildren(this, clone);

    Long sig = encoder.encode(clone);
    T out;
    if (!sig.equals(object.getId())) {
      clone.setId(sig);
      T read = read(clone);
      if (read == null) {
        manager.persist(clone);
        try {
          manager.flush();
        } catch (Exception e) {
          e.printStackTrace();
        }
        out = clone;
      } else {
        out = read;
      }
    } else {
      out = object;
    }

    return out;
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
