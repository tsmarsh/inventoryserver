package com.tailoredshapes.inventoryserver.repositories;

public interface Finder<T, Z> {
  T find(Z db);
}



