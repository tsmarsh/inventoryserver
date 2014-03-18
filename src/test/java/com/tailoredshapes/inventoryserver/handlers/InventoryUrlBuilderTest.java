package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InventoryUrlBuilderTest {
    @Test
    public void testShouldReturnTheCorrectUrlForAnInventory() throws Exception {
        User user = new UserBuilder().id(51284l).build();
        Inventory inventory = new InventoryBuilder()
                .id(141211l).build();

        SimpleScope scope = GuiceTest.injector.getInstance(SimpleScope.class);
        scope.enter();
        try {
            scope.seed(User.class, user);
            UrlBuilder<Inventory> inventoryUrlBuilder = GuiceTest.injector.getInstance(new Key<UrlBuilder<Inventory>>() {});
            String url = inventoryUrlBuilder.build(inventory);
            assertEquals("http://localhost:5555/users/51284/inventories/141211", url);

        } finally {
            scope.exit();
        }
    }
}
