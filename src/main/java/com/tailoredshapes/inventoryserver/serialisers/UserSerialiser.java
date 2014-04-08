package com.tailoredshapes.inventoryserver.serialisers;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

public class UserSerialiser implements Serialiser<User, byte[]> {

    private final UrlBuilder<User> urlBuilder;
    private final Serialiser<Inventory, String> inventorySerialiser;

    @Inject
    public UserSerialiser(UrlBuilder<User> urlBuilder, Serialiser<Inventory, String> inventorySerialiser) {
        this.urlBuilder = urlBuilder;
        this.inventorySerialiser = inventorySerialiser;
    }

    @Override
    public byte[] serialise(User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", urlBuilder.build(user));
        jsonObject.put("name", user.getName());
        jsonObject.put("publicKey", Base64.encode(user.getPublicKey().getEncoded()));

        JSONArray inventories = new JSONArray();
        for (Inventory inventory : user.getInventories()) {
            inventories.put(new JSONObject(inventorySerialiser.serialise(inventory)));
        }

        jsonObject.put("inventories", inventories);

        return jsonObject.toString().getBytes();
    }
}


