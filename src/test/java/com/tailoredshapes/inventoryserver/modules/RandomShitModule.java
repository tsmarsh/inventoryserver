package com.tailoredshapes.inventoryserver.modules;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.extractors.InventoryIdExtractor;
import com.tailoredshapes.inventoryserver.extractors.UserIdExtractor;
import com.tailoredshapes.inventoryserver.extractors.UserNameExtractor;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.repositories.InventoryCategoryPredicate;
import com.tailoredshapes.inventoryserver.security.RSA;
import com.tailoredshapes.inventoryserver.validators.InventoryValidator;
import com.tailoredshapes.inventoryserver.validators.UserValidator;
import com.tailoredshapes.inventoryserver.validators.Validator;

import java.util.function.Predicate;

public class RandomShitModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(new TypeLiteral<Parser<User>>() {})
                .to(UserParser.class);

        binder.bind(new TypeLiteral<IdExtractor<Long, User>>() {})
                .to(UserIdExtractor.class);

        binder.bind(new TypeLiteral<IdExtractor<String, User>>() {})
                .to(UserNameExtractor.class);

        binder.bind(new TypeLiteral<IdExtractor<Long, Inventory>>() {})
                .to(InventoryIdExtractor.class);

        binder.bind(new TypeLiteral<Saver<User>>() {})
                .to(new TypeLiteral<UserSaver<RSA>>() {});

        binder.bind(new TypeLiteral<Saver<Inventory>>() {})
                .to(InventorySaver.class);

        binder.bind(new TypeLiteral<Saver<Metric>>() {})
                .to(MetricSaver.class);

        binder.bind(new TypeLiteral<Saver<MetricType>>() {})
                .to(new TypeLiteral<ChildFreeSaver<MetricType>>() {});

        binder.bind(new TypeLiteral<Predicate<Inventory>>() {})
                .to(InventoryCategoryPredicate.class);

        binder.bind(new TypeLiteral<Validator<User>>() {})
                .to(UserValidator.class);

        binder.bind(new TypeLiteral<Validator<Inventory>>() {})
                .to(InventoryValidator.class);
    }
}
