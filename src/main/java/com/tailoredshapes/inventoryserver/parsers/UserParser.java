package com.tailoredshapes.inventoryserver.parsers;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

public class UserParser implements Parser<User> {

    private final UserRepository repo;
    private InventoryParser inventoryParser;


    @Inject
    public UserParser(UserRepository repo, InventoryParser inventoryParser) {
        this.repo = repo;
        this.inventoryParser = inventoryParser;
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

        Set<Inventory> inventorySet = new HashSet<>();

        if (jsonUser.has("inventories")) {
            JSONArray inventories = jsonUser.getJSONArray("inventories");
            for (int i = 0; i < inventories.length(); i++) {
                inventorySet.add(inventoryParser.parse(inventories.getJSONObject(i).toString()));
            }
        }

        user.setInventories(inventorySet);

        return user;
    }
}
