package com.tailoredshapes.inventoryserver.dao;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.model.Keyed;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.utils.RSA;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;


public class RSAEncoder<T extends Keyed> implements Encoder<T, RSA> {

    private Serialiser<T> serialiser;
    private ByteArrayToLong shrinker;

    @Inject
    public RSAEncoder(Serialiser<T> serialiser, ByteArrayToLong shrinker) {
        this.serialiser = serialiser;
        this.shrinker = shrinker;
    }

    @Override
    public Long encode(T object) {

        Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(object.getPrivateKey());
            signature.update(serialiser.serialise(object));
            return shrinker.shrink(signature.sign());

        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return 0l;
    }


}

