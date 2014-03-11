package com.tailoredshapes.inventoryserver.encoders;

import com.google.inject.Inject;
import com.tailoredshapes.inventoryserver.security.SHA;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAEncoder<T> implements Encoder<T, SHA> {

    private final Serialiser<T> serialiser;
    private final ByteArrayToLong shrinker;

    @Inject
    public SHAEncoder(Serialiser<T> serialiser, ByteArrayToLong shrinker) {
        this.serialiser = serialiser;
        this.shrinker = shrinker;
    }

    @Override
    public Long encode(T object) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA1");
            shrinker.shrink(digest.digest(serialiser.serialise(object)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return 0l;
    }
}
