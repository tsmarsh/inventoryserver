package com.tailoredshapes.inventoryserver.model;

/**
 * Created by tmarsh on 2/17/14.
 */
public interface Idable<T> {
    Long getId();
    T setId(Long id);
}
