package com.tailoredshapes.inventoryserver.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;

public class InventoryRootUrlBuilders implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<UrlBuilder<User>>() {})
                .to(UserUrlBuilder.class);

        binder.bind(new TypeLiteral<UrlBuilder<Inventory>>() {})
                .to(InventoryUrlBuilder.class);
    }
}
