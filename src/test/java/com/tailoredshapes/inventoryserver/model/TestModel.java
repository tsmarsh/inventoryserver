package com.tailoredshapes.inventoryserver.model;

import com.tailoredshapes.inventoryserver.model.Idable;

public class TestModel implements Idable<TestModel> {

    private Long id = 0l;
    private String value;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public TestModel setId(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public TestModel setValue(String value) {
        this.value = value;
        return this;
    }
}
