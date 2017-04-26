package com.tailoredshapes.inventoryserver.repositories;

import java.util.function.Predicate;

import com.tailoredshapes.inventoryserver.model.Inventory;

public class InventoryCategoryPredicate implements Predicate<Inventory> {

  private final Inventory t;

  public InventoryCategoryPredicate(Inventory t) {
    this.t = t;
  }

  @Override
  public boolean test(Inventory underTest) {
    return t.getCategory().equals(underTest.getCategory());
  }
}
