package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.builders.CategoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.tailoredshapes.underbar.UnderBar.list;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class InventorySaverTest {

    @Mock
    DAO<Inventory> inventoryDAO;

    @Mock
    DAO<Category> categoryDAO;

    @Mock
    DAO<Metric> metricDAO;

    @Test
    public void shouldSaveChildren() throws Exception {
        Metric metric = new MetricBuilder().build();
        Category category = new CategoryBuilder().build();
        Inventory inventory = new InventoryBuilder().category(category).metrics(list(metric)).build();

        InventorySaver inventorySaver = new InventorySaver(metricDAO, categoryDAO);
        when(categoryDAO.upsert(category)).thenReturn(category);
        when(metricDAO.upsert(metric)).thenReturn(metric);

        inventorySaver.saveChildren(inventoryDAO, inventory);

        verify(inventoryDAO).upsert(null);
        verify(categoryDAO).upsert(category);
        verify(metricDAO).upsert(metric);
    }
}