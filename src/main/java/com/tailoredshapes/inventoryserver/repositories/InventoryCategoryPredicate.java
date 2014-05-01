package com.tailoredshapes.inventoryserver.repositories;

import com.google.inject.servlet.RequestScoped;
import com.tailoredshapes.inventoryserver.model.Inventory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.function.Predicate;

@RequestScoped
public class InventoryCategoryPredicate implements Predicate<Inventory> {

    private final Inventory t;

    @Inject
    public InventoryCategoryPredicate(@Named("current_inventory") Inventory t) {
        this.t = t;
    }

    @Override
    public boolean test(Inventory underTest) {
        return t.getCategory().equals(underTest.getCategory());
    }
}
