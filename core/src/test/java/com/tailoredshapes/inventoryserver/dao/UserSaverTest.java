package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.tailoredshapes.underbar.UnderBar.list;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserSaverTest {

    @Mock
    DAO<Inventory> inventoryDAO;

    @Mock
    DAO<User> userDAO;

    @Test
    public void shouldSaveTheChildObjects() throws Exception {
        Inventory inventory = new InventoryBuilder().build();
        User user = new UserBuilder().inventories(list(inventory)).build();
        UserSaver userSaver = new UserSaver(inventoryDAO);

        when(inventoryDAO.upsert(inventory)).thenReturn(inventory);

        userSaver.saveChildren(userDAO, user);
        verify(inventoryDAO).upsert(inventory);
    }
}