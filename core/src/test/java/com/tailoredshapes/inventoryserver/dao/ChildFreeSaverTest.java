package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.TestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ChildFreeSaverTest {

    @Mock
    DAO<TestModel> dao;

    @Test
    public void saveChildren() throws Exception {
        ChildFreeSaver<TestModel> saver = new ChildFreeSaver<>();
        TestModel testModel = new TestModel();
        TestModel model = saver.saveChildren(dao, testModel);

        assertEquals(model, testModel);
    }

}