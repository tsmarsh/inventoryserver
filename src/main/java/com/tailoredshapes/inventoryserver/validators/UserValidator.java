package com.tailoredshapes.inventoryserver.validators;

import com.tailoredshapes.inventoryserver.model.User;

public class UserValidator implements Validator<User> {
    @Override
    public Boolean validate(User user) {
        return user.getName() != null;
    }
}

