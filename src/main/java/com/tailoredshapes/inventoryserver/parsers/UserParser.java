package com.tailoredshapes.inventoryserver.parsers;

import com.google.inject.servlet.RequestScoped;
import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@RequestScoped
public class UserParser implements Parser<User> {

    private final Repository<User, ?> repo;
    private final Parser<Inventory> inventoryParser;
    private final IdExtractor<User> idExtractor;


    @Inject
    public UserParser(Repository<User, ?> repo, Parser<Inventory> inventoryParser, IdExtractor<User> idExtractor) {
        this.repo = repo;
        this.inventoryParser = inventoryParser;
        this.idExtractor = idExtractor;
    }

    @Override
    public User parse(String s) {
        JSONObject jsonUser = new JSONObject(s);
        User user = new User();
        if (jsonUser.has("id")) {
            String id = jsonUser.getString("id");
            try {
                user = repo.findById(idExtractor.extract(new URL(id).getPath()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
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
