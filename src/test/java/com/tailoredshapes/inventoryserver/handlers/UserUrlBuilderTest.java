package com.tailoredshapes.inventoryserver.handlers;

import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.UserBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserUrlBuilderTest {
    @Test
    public void testBuild() throws Exception {
        UserUrlBuilder builder = new UserUrlBuilder("http", "tailoredshapes.com", 80);
        User user = new UserBuilder().id(39291l).build();
        assertEquals("http://tailoredshapes.com:80/users/39291", builder.build(user));
    }
}
