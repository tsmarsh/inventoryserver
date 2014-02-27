package com.tailoredshapes.inventoryserver.dao;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.tailoredshapes.inventoryserver.model.User;
import org.json.JSONObject;

public class UserSerialiser implements Serialiser<User> {

    @Override
    public byte[] serialise(User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", user.getId());
        jsonObject.put("name", user.getName());
        jsonObject.put("publicKey", Base64.encode(user.getPublicKey().getEncoded()));
        return jsonObject.toString().getBytes();
    }
}
