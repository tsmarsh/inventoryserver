package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.User;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

public class RSAEncoder implements Encoder {

    @Override
    public Long encode(User user, byte[] bits) {


        Signature signature = null;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(user.getPrivateKey());
            signature.update(bits);
            byte[] sign = signature.sign();
            long value = 0;

            for (int i = 0; i < 8; i++)
            {
                value += ((long) sign[i] & 0xffL) << (8 * i);
            }
            return value;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return 0l;
    }
}
