package com.tailoredshapes.inventoryserver.repositories.memory;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import org.junit.Test;

import static com.tailoredshapes.inventoryserver.GuiceTest.injector;
import static org.junit.Assert.assertEquals;
public class CategoryRepositoryTest{


    @Test
    public void testFindByName() throws Exception {
        Category category = new CategoryBuilder().build();
        DAO<Category> dao = injector.getInstance(new Key<DAO<Category>>(){});
        CategoryRepository repo = injector.getInstance(CategoryRepository.class);
        Category savedCategory = dao.create(category);
        Category byId = repo.findByFullname(savedCategory.getFullname());
        assertEquals(savedCategory, byId);
    }
}
