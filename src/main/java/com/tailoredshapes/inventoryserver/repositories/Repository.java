package com.tailoredshapes.inventoryserver.repositories;

import java.util.Collection;

public interface Repository<T> {
    T findById(Long extract);

    Collection<T> list();
}
