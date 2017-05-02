package com.tailoredshapes.inventoryserver.encoders;

@FunctionalInterface
public interface Encoder {
  Long encode(Object object);
}
