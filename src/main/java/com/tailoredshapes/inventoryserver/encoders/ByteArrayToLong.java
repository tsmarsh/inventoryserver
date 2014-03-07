package com.tailoredshapes.inventoryserver.encoders;

public class ByteArrayToLong {
    public Long shrink(byte[] sign) {
        long value = 0;

        for (int i = 0; i < 8; i++) {
            value += ((long) sign[i] & 0xffL) << (8 * i);
        }
        return value;
    }
}
