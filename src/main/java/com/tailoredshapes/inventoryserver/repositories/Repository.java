package com.tailoredshapes.inventoryserver.repositories;

public interface Repository<T> {
    T findById(Long extract);
}
