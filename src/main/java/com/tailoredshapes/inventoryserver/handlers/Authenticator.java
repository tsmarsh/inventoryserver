package com.tailoredshapes.inventoryserver.handlers;

import com.google.inject.Inject;
import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;

public class Authenticator {
    private final UserRepository userRepository;
    private final IdExtractor userIdExtractor;

    @Inject
    public Authenticator(UserRepository userRepository, IdExtractor<User> userIdExtractor) {
        this.userRepository = userRepository;
        this.userIdExtractor = userIdExtractor;
    }

    public User authenticate(HttpExchange httpExchange) {
        Long extract = userIdExtractor.extract(httpExchange);
        return userRepository.findById(extract);
    }
}

