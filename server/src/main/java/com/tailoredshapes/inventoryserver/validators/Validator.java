package com.tailoredshapes.inventoryserver.validators;

public interface Validator<T> {
  Boolean validate(T t);
}
