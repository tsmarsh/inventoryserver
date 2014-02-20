package com.tailoredshapes.inventoryserver.utils;

import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.repositories.CategoryRepository;
import com.tailoredshapes.inventoryserver.repositories.InventoryRepository;
import com.tailoredshapes.inventoryserver.repositories.MetricTypeRepository;
import com.tailoredshapes.inventoryserver.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class InventoryParserTest {

    @Mock private UserRepository userRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private InventoryRepository inventoryRepository;
    @Mock private MetricTypeRepository metricTypeRepository;

    @Mock private User testUser;
    @Mock private Category testCategory;
    @Mock private Inventory testParentInventory;
    @Mock private MetricType testType;


    @Before
    public void init(){
        when(userRepository.findById(1)).thenReturn(testUser);
        when(categoryRepository.findByFullname("com.tailoredshapes")).thenReturn(testCategory);
        when(inventoryRepository.findById(666)).thenReturn(testParentInventory);
        when(metricTypeRepository.findByName("string")).thenReturn(testType);
    }

    @Test
    public void shouldParseASimpleInventory() throws Exception {
        InventoryParser parser = new InventoryParser(userRepository, categoryRepository, inventoryRepository, metricTypeRepository);
        String inventoryJSON = "{user_id: 1, category: \"com.tailoredshapes\"}";

        Inventory inv = parser.parse(inventoryJSON);
        assertEquals(inv.getCategory(), testCategory);
        assertEquals(inv.getUser(), testUser);
    }

    @Test
    public void shouldParseAnInventoryWithParent() throws Exception {
        InventoryParser parser = new InventoryParser(userRepository, categoryRepository, inventoryRepository, metricTypeRepository);
        String inventoryJSON = "{user_id: 1, category: \"com.tailoredshapes\", parent_id: 666}";

        Inventory inv = parser.parse(inventoryJSON);
        assertEquals(inv.getParent(), testParentInventory);
    }

    @Test
    public void shouldParseAnInventoryWithMetrics() throws Exception {
        InventoryParser parser = new InventoryParser(userRepository, categoryRepository, inventoryRepository, metricTypeRepository);
        String inventoryJSON = "{user_id: 1, category: \"com.tailoredshapes\", metrics: [{type: \"string\", value: \"Cassie\"}, {type: \"string\", value: \"Archer\"}]}";

        Inventory inv = parser.parse(inventoryJSON);
        assertEquals(inv.getMetrics().get(0).getValue(), "Cassie");
        assertEquals(inv.getMetrics().get(0).getType(), testType);
        assertEquals(inv.getMetrics().get(1).getValue(), "Archer");
        assertEquals(inv.getMetrics().get(1).getType(), testType);
    }
}
