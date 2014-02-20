package com.tailoredshapes.inventoryserver.utils;

import java.io.InputStream;

/**
 * Created by tmarsh on 2/16/14.
 */
public interface Parser<T> {
    T parse(String s);
}
