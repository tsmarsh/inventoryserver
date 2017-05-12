package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.repositories.Finder;
import com.tailoredshapes.inventoryserver.repositories.Looker;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategorySaverTest {

    @Mock
    Repository.FindBy<Category, Object> findBy;

    @Mock
    Looker<String, Category, Object> fullNameFinderFactor;

    @Mock
    Finder<Category, Object> finder;

    @Mock
    DAO<Category> categoryDAO;

    @Test
    public void shouldSaveParents() throws Exception {
        Category parent = new CategoryBuilder().name("bar").fullname("bar").build();
        Category category = new CategoryBuilder().name(null).fullname("bar.foo").build();

        CategorySaver<Object> saver = new CategorySaver<>(findBy, fullNameFinderFactor);

        when(fullNameFinderFactor.lookFor("bar")).thenReturn(finder);
        when(findBy.findBy(finder)).thenReturn(parent);
        when(categoryDAO.upsert(parent)).thenReturn(parent);

        Category savedCategory = saver.saveChildren(categoryDAO, category);

        verify(fullNameFinderFactor).lookFor("bar");

        assertEquals(parent, savedCategory.getParent());
        assertEquals("foo", savedCategory.getName());
    }
}