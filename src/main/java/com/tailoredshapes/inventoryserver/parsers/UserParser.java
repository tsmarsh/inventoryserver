package com.tailoredshapes.inventoryserver.parsers;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import org.json.JSONObject;

public class UserParser implements Parser<User> {

    private final UserRepository repo;

    @Inject
    public UserParser(UserRepository repo) {

        this.repo = repo;
    }

    @Override
    public User parse(String s) {
        JSONObject jsonUser = new JSONObject(s);
        User user = new User();
        if (jsonUser.has("id")) {
            long id = jsonUser.getLong("id");
            user = repo.findById(id);
        }

        String name = jsonUser.getString("name");
        user.setName(name);
        return user;
    }
}
