package com.tailoredshapes.inventoryserver.repositories.memory;

import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InMemoryCategoryRepositoryTest {


    private Serialiser<Category> serialiser;
    private Encoder encoder;
    private InMemoryDAO<Category> dao;

    @Before
    public void setUp() throws Exception {
        serialiser = new JSONSerialiser<>();
        encoder = new RSAEncoder();
        dao = new InMemoryDAO<>(serialiser, encoder);
    }

    @Test
    public void testFindByName() throws Exception {
        User user = new UserBuilder().build();
        Category category = new CategoryBuilder().build();
        InMemoryCategoryRepository repo = new InMemoryCategoryRepository(dao);
        Category savedCategory = dao.create(user, category);
        Category byId = repo.findByFullname(user, savedCategory.getFullname());
        assertEquals(savedCategory, byId);
    }
}
