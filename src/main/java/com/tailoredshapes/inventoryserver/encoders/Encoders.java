package com.tailoredshapes.inventoryserver.encoders;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Encoders {

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

  Encoder shaEncoder = (object) -> {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA1");
      return ByteArrayToLong.shrink(digest.digest(object.toString().getBytes()));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return 0l;
  };
}
