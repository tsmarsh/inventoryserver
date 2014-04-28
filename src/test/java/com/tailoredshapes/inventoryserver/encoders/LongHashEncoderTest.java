package com.tailoredshapes.inventoryserver.encoders;

import com.google.inject.Key;
import com.tailoredshapes.inventoryserver.GuiceTest;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.builders.MetricTypeBuilder;
import org.junit.Test;

import static org.junit.Assert.assertNotSame;

public class LongHashEncoderTest {
    @Test
    public void testEncode() throws Exception {
        MetricType metricType = new MetricTypeBuilder().build();

        LongHashEncoder<MetricType> instance = GuiceTest.injector.getInstance(new Key<LongHashEncoder<MetricType>>() {});
        Long encode = instance.encode(metricType);

        assertNotSame(30582808516010022l, encode); //This is a fragile test
    }
}
