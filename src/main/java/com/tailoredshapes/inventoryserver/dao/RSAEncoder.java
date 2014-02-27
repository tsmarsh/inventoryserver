package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.utils.RSA;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

public class RSAEncoder implements Encoder<RSA> {

    @Override
    public Long encode(User user, byte[] bits) {


        Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(user.getPrivateKey());
            signature.update(bits);
            byte[] sign = signature.sign();
            long value = 0;

            for (int i = 0; i < 8; i++) {
                value += ((long) sign[i] & 0xffL) << (8 * i);
            }
            return value;

        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return 0l;
    }
}
