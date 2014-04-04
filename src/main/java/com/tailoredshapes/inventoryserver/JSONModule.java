package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.responders.JSONResponder;
import com.tailoredshapes.inventoryserver.responders.Responder;
import com.tailoredshapes.inventoryserver.serialisers.*;

public class JSONModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<Serialiser<Inventory>>() {})
                .to(InventorySerialiser.class);

        binder.bind(new TypeLiteral<Serialiser<Category>>() {})
                .to(new TypeLiteral<JSONSerialiser<Category>>() {});

        binder.bind(new TypeLiteral<Serialiser<User>>() {})
                .to(UserSerialiser.class);

        binder.bind(new TypeLiteral<Serialiser<Metric>>() {})
                .to(MetricSerialiser.class);

        binder.bind(new TypeLiteral<Serialiser<MetricType>>() {})
                .to(new TypeLiteral<JSONSerialiser<MetricType>>() {});

        binder.bind(new TypeLiteral<Responder<Inventory>>() {})
                .to(new TypeLiteral<JSONResponder<Inventory>>() {});

        binder.bind(new TypeLiteral<Responder<User>>() {})
                .to(new TypeLiteral<JSONResponder<User>>() {});
    }
}
