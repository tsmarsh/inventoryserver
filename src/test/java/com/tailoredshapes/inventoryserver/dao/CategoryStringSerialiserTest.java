package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.serialisers.CategoryStringSerialiser;
import org.json.JSONObject;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class CategoryStringSerialiserTest {

    @Test
    public void testSerialise() throws Exception {
        Category category = new CategoryBuilder().id(555l).build();

        CategoryStringSerialiser categoryStringSeriliser = new CategoryStringSerialiser();

        JSONObject jsonObject = new JSONObject(categoryStringSeriliser.serialise(category));
        assertThat(jsonObject.getLong("id")).isEqualTo(category.getId());
        assertThat(jsonObject.getString("name")).isEqualTo(category.getName());
        assertThat(jsonObject.getString("fullname")).isEqualTo(category.getFullname());
    }
}

