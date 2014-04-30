package com.tailoredshapes.inventoryserver.extractors;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.sun.net.httpserver.HttpExchange;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.FinderFactory;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class UrlIdExtractor<Z> implements IdExtractor<User> {

    private final Pattern userIdPattern = Pattern.compile("/users/(\\w+)/?(-?\\d+)?/?.*$");
    private final Repository<User, Z> repo;
    private final FinderFactory<User, String, Z> finderFactory;

    @Inject
    public UrlIdExtractor(Repository<User, Z> repo, FinderFactory<User, String, Z> finderFactory) {
        this.repo = repo;
        this.finderFactory = finderFactory;
    }

    @Override
    public Long extract(HttpExchange exchange) {
        URI requestURI = exchange.getRequestURI();
        String path = requestURI.getPath();
        return extract(path);
    }

    @Override
    public Long extract(String path) {
        Matcher matcher = userIdPattern.matcher(path);
        Long id = null;
        if (matcher.matches()) {
            if (matcher.group(2) != null) {
                id = Long.parseLong(matcher.group(2));
            } else {
                User by = repo.findBy(finderFactory.lookFor(matcher.group(1)));
                id = by != null ? by.getId() : null;
            }
        }

        return id;
    }
}
