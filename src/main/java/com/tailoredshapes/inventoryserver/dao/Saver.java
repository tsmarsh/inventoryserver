package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Idable;

public abstract class Saver<T> {
    public <Z extends Idable<Z>> void upsert(Z object, DAO<Z> dao) {
        if (object != null) {
            if (null == object.getId()) {
                dao.create(object);
            } else {
                dao.update(object);
            }
        }
    }

    public abstract T saveChildren(T object);
}
