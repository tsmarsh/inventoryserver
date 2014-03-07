package com.tailoredshapes.inventoryserver.repositories.memory;

import com.tailoredshapes.inventoryserver.dao.memory.InMemoryChildFreeDAO;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.encoders.ByteArrayToLong;
import com.tailoredshapes.inventoryserver.encoders.Encoder;
import com.tailoredshapes.inventoryserver.encoders.SHAEncoder;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.serialisers.JSONSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.security.SHA;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InMemoryCategoryRepositoryTest {


    private InMemoryDAO<Category, SHA> dao;

    @Before
    public void setUp() throws Exception {
        Serialiser<Category> serialiser = new JSONSerialiser<>();
        Encoder<Category, SHA> encoder = new SHAEncoder<>(serialiser, new ByteArrayToLong());
        dao = new InMemoryChildFreeDAO<>(encoder);
    }

    @Test
    public void testFindByName() throws Exception {
        Category category = new CategoryBuilder().build();
        InMemoryCategoryRepository<SHA> repo = new InMemoryCategoryRepository<>(dao);
        Category savedCategory = dao.create(category);
        Category byId = repo.findByFullname(savedCategory.getFullname());
        assertEquals(savedCategory, byId);
    }
}
