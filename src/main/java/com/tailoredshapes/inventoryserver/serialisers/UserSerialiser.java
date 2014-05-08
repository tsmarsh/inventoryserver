package com.tailoredshapes.inventoryserver.serialisers;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.Base64;


public class UserSerialiser implements Serialiser<User, byte[]> {

    private final Serialiser<Inventory, byte[]> inventorySerialiser;

    @Inject
    public UserSerialiser(Serialiser<Inventory, byte[]> inventorySerialiser) {
        this.inventorySerialiser = inventorySerialiser;
    }

    @Override
    public byte[] serialise(User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", user.getName());
        jsonObject.put("publicKey", Base64.getEncoder().encodeToString(user.getPublicKey().getEncoded()));

        JSONArray inventories = new JSONArray();
        for (Inventory inventory : user.getInventories()) {
            inventories.put(new JSONObject(inventorySerialiser.serialise(inventory)).toString());
        }

        jsonObject.put("inventories", inventories);

        return jsonObject.toString().getBytes();
    }
}


