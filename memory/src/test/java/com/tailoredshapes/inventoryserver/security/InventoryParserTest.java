package com.tailoredshapes.inventoryserver.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import com.tailoredshapes.inventoryserver.dao.CategorySaver;
import com.tailoredshapes.inventoryserver.dao.InventorySaver;
import com.tailoredshapes.inventoryserver.dao.MetricSaver;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.encoders.Encoders;
import com.tailoredshapes.inventoryserver.extractors.Extractors;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.model.builders.InventoryBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricBuilder;
import com.tailoredshapes.inventoryserver.model.builders.MetricTypeBuilder;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryLookers;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryRepository;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;

import org.junit.Before;
import org.junit.Test;

import static com.tailoredshapes.inventoryserver.serialisers.Serialisers.inventorySerializerBuilder;
import static com.tailoredshapes.inventoryserver.serialisers.Serialisers.metricSerialiser;
import static junit.framework.Assert.assertEquals;

public class InventoryParserTest {

    Gson gson = new Gson();

    private Repository.FindBy<Category, Map<Long, Category>>
            iMcategoryFindBy;
    private Repository.FindById<Inventory>
            iMinventoryFindById;
    private Repository.FindBy<MetricType, Map<Long, MetricType>>
            iMmetricTypeFindBy;
    private InMemoryDAO<MetricType>
            iMMetricTypeDAO;
    private InMemoryDAO<Metric>
            iMMetricDAO;
    private InMemoryDAO<Inventory>
            dao;
    private Parser<Inventory>
            parser;
    private InMemoryDAO<Category>
            iMCatDAO;
    private Serialiser<Inventory> serialiser;

    @Before
    public void setUp() throws Exception {
        Map<Long, Category> catdb = new HashMap<>();
        Map<Long, Inventory> invdb = new HashMap<>();
        Map<Long, Metric> metdb = new HashMap<>();
        Map<Long, MetricType> metTypedb = new HashMap<>();
        Map<Long, User> udb = new HashMap<>();

        iMcategoryFindBy = InMemoryRepository.findBy(catdb);
        iMinventoryFindById = InMemoryRepository.findById(invdb);
        iMmetricTypeFindBy = InMemoryRepository.findBy(metTypedb);

        iMMetricTypeDAO = new InMemoryDAO<>(metTypedb, Encoders.shaEncoder, (x, t) -> t);
        iMMetricDAO = new InMemoryDAO<>(metdb, Encoders.shaEncoder, new MetricSaver(iMMetricTypeDAO));
        iMCatDAO = new InMemoryDAO<>(catdb,
                Encoders.shaEncoder,
                new CategorySaver<>(iMcategoryFindBy, InMemoryLookers.categoryByFullName));
        dao = new InMemoryDAO<>(invdb, Encoders.shaEncoder, new InventorySaver(iMMetricDAO, iMCatDAO));
        parser = InventoryParser.inventoryParser(iMcategoryFindBy,
                iMinventoryFindById,
                iMmetricTypeFindBy, InMemoryLookers.categoryByFullName,
                InMemoryLookers.metricTypeByName,
                Extractors.inventoryExtractor);

        serialiser =
                inventorySerializerBuilder.apply(new InventoryUrlBuilder("http", "localhost", 5555), metricSerialiser);

    }

    @Test
    public void shouldParseASimpleInventory() throws Exception {
        Inventory inventory = new InventoryBuilder().build();

        Inventory inv = parser.parse(serialiser.serialise(inventory));
        assertEquals(inventory.getCategory().getFullname(), inv.getCategory().getFullname());
    }

    @Test
    public void shouldParseAnInventoryWithParent() throws Exception {
        Inventory parent = new InventoryBuilder().build();
        parent = dao.create(parent);

        Inventory inventory = new InventoryBuilder().parent(parent).build();

        Inventory inv = parser.parse(serialiser.serialise(inventory));

        assertEquals(parent, inv.getParent());

    }

    @Test
    public void shouldParseAnInventoryWithMetrics() throws Exception {
        List<Metric> metrics = new ArrayList<>(2);
        MetricType testType = new MetricTypeBuilder().build();
        Metric metric1 = new MetricBuilder().type(testType).value("Cassie").build();
        Metric metric2 = new MetricBuilder().type(testType).value("Archer").build();
        metrics.add(metric1);
        metrics.add(metric2);

        Inventory inventory = new InventoryBuilder().metrics(metrics).build();

        Inventory inv = parser.parse(serialiser.serialise(inventory));

        assertEquals(inv.getMetrics().get(0).getValue(), "Cassie");
        assertEquals(inv.getMetrics().get(0).getType().getName(), testType.getName());
        assertEquals(inv.getMetrics().get(1).getValue(), "Archer");
        assertEquals(inv.getMetrics().get(1).getType().getName(), testType.getName());
    }
}
