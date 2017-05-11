package com.tailoredshapes.inventoryserver.extractors;

@FunctionalInterface
public interface IdExtractor<S> {
  S extract(String path);
}

