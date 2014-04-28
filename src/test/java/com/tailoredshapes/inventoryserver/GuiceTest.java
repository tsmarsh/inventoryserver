package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.scopes.SimpleScope;

public class GuiceTest {

    public static Injector injector = Guice.createInjector(
            new InventoryServerModule("localhost", 5555),
            new InMemoryModule(),
            new JSONModule(),
            new EncoderModule(),
            new Module() {

                @Override
                public void configure(Binder binder) {
                    SimpleScope requestScope = new SimpleScope();
                    binder.bindScope(RequestScoped.class, requestScope);
                    binder.bind(SimpleScope.class).toInstance(requestScope);
                    binder.bind(User.class).annotatedWith(Names.named("current_user"))
                            .toProvider(SimpleScope.<User>seededKeyProvider())
                            .in(RequestScoped.class);
                    binder.bind(Inventory.class)
                            .annotatedWith(Names.named("current_inventory"))
                            .toProvider(SimpleScope.<Inventory>seededKeyProvider())
                            .in(RequestScoped.class);
                }
            });
}
