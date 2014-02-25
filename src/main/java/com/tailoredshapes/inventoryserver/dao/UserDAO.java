package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.User;

public interface UserDAO {
    User create(User user);

    User read(User user);

    User update(User user);

    User delete(User user);
}

