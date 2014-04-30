package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.encoders.RSAEncoder;
import com.tailoredshapes.inventoryserver.encoders.SHAEncoder;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.security.KeyProvider;
import com.tailoredshapes.inventoryserver.security.RSA;
import com.tailoredshapes.inventoryserver.security.RSAKeyProvider;

public class EncoderModule implements Module {
    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<Encoder<User, ?>>() {})
                .to(new TypeLiteral<RSAEncoder<User>>() {});

        binder.bind(new TypeLiteral<Encoder<Inventory, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<Inventory>>() {});

        binder.bind(new TypeLiteral<Encoder<Metric, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<Metric>>() {});

        binder.bind(new TypeLiteral<Encoder<MetricType, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<MetricType>>() {});

        binder.bind(new TypeLiteral<Encoder<Category, ?>>() {})
                .to(new TypeLiteral<SHAEncoder<Category>>() {});

        binder.bind(new TypeLiteral<KeyProvider<RSA>>() {})
                .to(RSAKeyProvider.class);
    }
}
