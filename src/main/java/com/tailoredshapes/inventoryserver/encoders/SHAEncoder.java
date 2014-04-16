package com.tailoredshapes.inventoryserver.encoders;

import javax.inject.Inject;
import com.tailoredshapes.inventoryserver.security.SHA;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAEncoder<T> implements Encoder<T, SHA> {

    private final Serialiser<T, byte[]> serialiser;

    @Inject
    public SHAEncoder(Serialiser<T, byte[]> serialiser) {
        this.serialiser = serialiser;
    }

    @Override
    public Long encode(T object) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            return ByteArrayToLong.shrink(digest.digest(serialiser.serialise(object)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return 0l;
    }
}
