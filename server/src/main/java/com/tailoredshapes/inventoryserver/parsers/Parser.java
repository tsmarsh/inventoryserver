package com.tailoredshapes.inventoryserver.parsers;

public interface Parser<T> {
  T parse(String s);
}
