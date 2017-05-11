package com.tailoredshapes.inventoryserver.repositories;

/**
 * Created by marshto3 on 4/24/17.
 */
public interface Looker<T, R, S> {
  Finder<R, S> lookFor(T t);
}

