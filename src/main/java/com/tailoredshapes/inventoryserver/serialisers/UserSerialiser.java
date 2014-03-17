package com.tailoredshapes.inventoryserver.serialisers;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

public class UserSerialiser implements Serialiser<User> {

    private Serialiser<Inventory> inventorySerialiser;

    @Inject
    public UserSerialiser(Serialiser<Inventory> inventorySerialiser) {
        this.inventorySerialiser = inventorySerialiser;
    }

    @Override
    public byte[] serialise(User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", user.getId());
        jsonObject.put("name", user.getName());
        jsonObject.put("publicKey", Base64.encode(user.getPublicKey().getEncoded()));

        JSONArray inventories = new JSONArray();
        for(Inventory inventory : user.getInventories()){
            inventories.put(new JSONObject(new String(inventorySerialiser.serialise(inventory))));
        }

        jsonObject.put("inventories", inventories);

        return jsonObject.toString().getBytes();
    }
}
