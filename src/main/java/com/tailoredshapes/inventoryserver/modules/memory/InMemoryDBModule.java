package com.tailoredshapes.inventoryserver.modules.memory;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.tailoredshapes.inventoryserver.model.*;

import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDBModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<Map<Long, User>>() {})
                .to(new TypeLiteral<ConcurrentHashMap<Long, User>>() {})
                .in(Singleton.class);

        binder.bind(new TypeLiteral<Map<Long, Inventory>>() {})
                .to(new TypeLiteral<ConcurrentHashMap<Long, Inventory>>() {})
                .in(Singleton.class);
        binder.bind(new TypeLiteral<Map<Long, Metric>>() {})
                .to(new TypeLiteral<ConcurrentHashMap<Long, Metric>>() {})
                .in(Singleton.class);

        binder.bind(new TypeLiteral<Map<Long, MetricType>>() {})
                .to(new TypeLiteral<ConcurrentHashMap<Long, MetricType>>() {})
                .in(Singleton.class);

        binder.bind(new TypeLiteral<Map<Long, Category>>() {})
                .to(new TypeLiteral<ConcurrentHashMap<Long, Category>>() {})
                .in(Singleton.class);
    }
}
