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

import static junit.framework.Assert.assertEquals;

public class InventoryParserTest {

    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private InventoryRepository inventoryRepository;
    private MetricTypeRepository metricTypeRepository;

    private User testUser = new User();
    private Category testCategory = new Category();
    private Inventory testParentInventory = new Inventory();
    private MetricType testType = new MetricType();


    @Before
    public void init() {
        userRepository = new UserRepository() {
            @Override
            public User findById(long user_id) {
                if (user_id == 1l) {
                    return testUser;
                }
                return null;
            }
        };

        categoryRepository = new CategoryRepository() {
            @Override
            public Category findByFullname(User user, String categoryFullName) {
                if (categoryFullName.equals("com.tailoredshapes")) {
                    return testCategory;
                }
                return null;
            }
        };

        inventoryRepository = new InventoryRepository() {
            @Override
            public Inventory findById(User user, Long id) {
                if (id == 666l) {
                    return testParentInventory;
                }
                return null;
            }
        };

        metricTypeRepository = new MetricTypeRepository() {
            @Override
            public MetricType findByName(User user, String name) {
                if (name.equals("string")) {
                    return testType;
                }
                return null;
            }
        };
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
