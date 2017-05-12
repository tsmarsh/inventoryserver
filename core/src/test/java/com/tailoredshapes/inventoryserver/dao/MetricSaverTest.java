package com.tailoredshapes.inventoryserver.dao;

import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricTypeBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MetricSaverTest {

    @Mock DAO<Metric> metricDAO;
    @Mock DAO<MetricType> metricTypeDAO;

    @Test
    public void shouldSaveMetricType() throws Exception {
        MetricType metricType = new MetricTypeBuilder().build();
        Metric metric = new MetricBuilder().type(metricType).build();

        MetricSaver metricSaver = new MetricSaver(metricTypeDAO);

        when(metricTypeDAO.upsert(metricType)).thenReturn(metricType);

        metricSaver.saveChildren(metricDAO, metric);

        verify(metricTypeDAO).upsert(metricType);
    }
}