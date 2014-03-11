package com.tailoredshapes.inventoryserver.dao;

public class ChildFreeSaver<T> extends Saver<T>{

    @Override
    public T saveChildren(T object) {
        return object;
    }
}

