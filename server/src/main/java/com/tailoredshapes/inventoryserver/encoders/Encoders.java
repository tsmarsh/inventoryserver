package com.tailoredshapes.inventoryserver.encoders;

import static com.tailoredshapes.underbar.Die.rethrow;
import static java.security.MessageDigest.getInstance;

public interface Encoders {

  static Long shrink(byte[] sign) {
    long value = 0;

    for (int i = 0; i < 8; i++) {
      value += ((long) sign[i] & 0xffL) << (8 * i);
    }
    return value;
  }

  Encoder longHash = (object) -> {
    char[] value = object.toString().toCharArray();

    long h = 0l;

    if (value.length > 0) {
      for (char aValue : value) {
        h = 524287l * h + aValue;
      }
    }
    return h;
  };

  Encoder shaEncoder = (object) ->
    rethrow(()-> shrink(
            getInstance("SHA1").digest(
                    object.toString().getBytes())),
            () -> "Your Java doesn't know how to SHA1");

}
