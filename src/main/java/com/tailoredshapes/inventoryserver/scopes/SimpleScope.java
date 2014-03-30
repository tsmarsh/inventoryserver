package com.tailoredshapes.inventoryserver.scopes;

import com.google.common.collect.Maps;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

public class SimpleScope implements Scope {

    private static final Provider<Object> SEEDED_KEY_PROVIDER = new Provider<Object>() {
        @Override
        public Object get() {
            return null;
        }
    };

    private final ThreadLocal<Map<Key<?>, Object>> values = new ThreadLocal<>();

    public void enter() {
        checkState(values.get() == null, "A scoping block is already in progress");
        values.set(Maps.<Key<?>, Object>newHashMap());
    }

    public void exit() {
        checkState(values.get() != null, "No scoping block in progress");
        values.remove();
    }

    public <T> void seed(Key<T> key, T value) {
        Map<Key<?>, Object> scopedObjects = getScopedObjectMap();
        checkState(!scopedObjects.containsKey(key), "A value for the key %s was " +
                "already seeded in this scope. Old value: %s New value: %s", key,
                scopedObjects.get(key), value);
        scopedObjects.put(key, value);
    }

    public <T> void seed(Class<T> clazz, T value) {
        seed(Key.get(clazz), value);
    }

    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
        return new Provider<T>() {
            @Override
            public T get() {
                Map<Key<?>, Object> scopedObjects = getScopedObjectMap();

                @SuppressWarnings("unchecked")
                T current = (T) scopedObjects.get(key);
                if (current == null && !scopedObjects.containsKey(key)) {
                    current = unscoped.get();
                    scopedObjects.put(key, current);
                }
                return current;
            }
        };
    }

    private <T> Map<Key<?>, Object> getScopedObjectMap() {
        Map<Key<?>, Object> scopedObjects = values.get();
        if (scopedObjects == null) {
            return new HashMap<>();
        }
        return scopedObjects;
    }

    /**
     * Returns a provider that always throws exception complaining that the object
     * in question must be seeded before it can be injected.
     *
     * @return typed provider
     */
    @SuppressWarnings({"unchecked"})
    public static <T> Provider<T> seededKeyProvider() {
        return (Provider<T>) SEEDED_KEY_PROVIDER;
    }
}
