package com.tailoredshapes.inventoryserver.encoders;

import com.tailoredshapes.inventoryserver.model.Keyed;
import com.tailoredshapes.inventoryserver.security.RSA;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;

import javax.inject.Inject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;


public class RSAEncoder<T extends Keyed> implements Encoder<T, RSA> {

    private final Serialiser<T, byte[]> serialiser;

    @Inject
    public RSAEncoder(Serialiser<T, byte[]> serialiser) {
        this.serialiser = serialiser;
    }

    @Override
    public Long encode(T object) {

        Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(object.getPrivateKey());
            signature.update(serialiser.serialise(object));
            return ByteArrayToLong.shrink(signature.sign());

        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return 0l;
    }
}

