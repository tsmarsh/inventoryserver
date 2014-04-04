package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Key;
import com.google.inject.name.Names;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InventoryUrlBuilderTest {

    private SimpleScope scope;


    @Before
    public void init() {
        scope = GuiceTest.injector.getInstance(SimpleScope.class);
        scope.enter();
        final User user = new UserBuilder().id(51284l).build();
        scope.seed(Key.get(User.class, Names.named("current_user")), user);
    }

    @After
    public void tearDown() throws Exception {
        scope.exit();
    }

    @Test
    public void testShouldReturnTheCorrectUrlForAnInventory() throws Exception {
        Inventory inventory = new InventoryBuilder()
                .id(141211l).build();

        UrlBuilder<Inventory> inventoryUrlBuilder = GuiceTest.injector.getInstance(new Key<UrlBuilder<Inventory>>() {});
        String url = inventoryUrlBuilder.build(inventory);
        assertEquals("http://localhost:5555/users/51284/inventories/141211", url);
    }

    @Test
    public void testShouldReturnNullIfIdNotSet() throws Exception {

        Inventory inventory = new InventoryBuilder()
                .id(null).build();
        UrlBuilder<Inventory> inventoryUrlBuilder = GuiceTest.injector.getInstance(new Key<UrlBuilder<Inventory>>() {});
        String url = inventoryUrlBuilder.build(inventory);
        assertNull( url);

    }
}
