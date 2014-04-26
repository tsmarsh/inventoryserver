package com.tailoredshapes.inventoryserver.repositories;

public interface FinderFactory<T, V, Z> {
    Finder<T, Z> lookFor(V... ts);
}
