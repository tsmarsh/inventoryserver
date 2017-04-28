package com.tailoredshapes.inventoryserver;

import java.util.function.Function;

import com.tailoredshapes.inventoryserver.dao.CategorySaver;
import com.tailoredshapes.inventoryserver.dao.ChildFreeSaver;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InventorySaver;
import com.tailoredshapes.inventoryserver.dao.MetricSaver;
import com.tailoredshapes.inventoryserver.dao.hibernate.HibernateDAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;
import com.tailoredshapes.inventoryserver.serialisers.InventoryStringSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.MetricStringSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;

import javax.persistence.EntityManager;

import static com.tailoredshapes.inventoryserver.encoders.Encoders.shaEncoder;
import static com.tailoredshapes.inventoryserver.extractors.Extractors.inventoryExtractor;
import static com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateLookers.catergoryByFullName;
import static com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateLookers.metricTypeByName;
import static com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository.findBy;
import static com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository.findById;

public class Providers {

  interface DAOProvider<T> extends Function<EntityManager, DAO<T>> {}

  public static DAOProvider<MetricType> metricTypeDAO = (em) ->
    new HibernateDAO<>(MetricType.class, em, new ChildFreeSaver<>(), shaEncoder);
  public static DAOProvider<Metric> metricDAO = (em) ->
    new HibernateDAO<>(Metric.class, em, new MetricSaver(metricTypeDAO.apply(em)), shaEncoder);
  public static DAOProvider<Category> categoryDAO = (em) ->
    new HibernateDAO<>(
      Category.class,
      em,
      new CategorySaver<>(findBy(em), catergoryByFullName),
      shaEncoder);

  public static DAOProvider<Inventory> inventoryDAO = (em) ->
    new HibernateDAO<>(Inventory.class,
                       em,
                       new InventorySaver(metricDAO.apply(em), categoryDAO.apply(em)),
                       shaEncoder);

  interface ParserProvider<T> extends Function<EntityManager, Parser<T>> {}

  public static ParserProvider<Inventory> inventoryParser = (em) -> InventoryParser.inventoryParser(
    findBy(em),
    findById(Inventory.class, em),
    findBy(em),
    catergoryByFullName,
    metricTypeByName,
    inventoryExtractor);

  public static String protocol = "http";
  public static String host = "localhost";
  public static int port = 1414;

  public static UrlBuilder<Inventory> inventoryUrlBuilder = new InventoryUrlBuilder(protocol, host, port);

  public static Serialiser<Inventory>  inventorySerialiser =
    new InventoryStringSerialiser(inventoryUrlBuilder, new MetricStringSerialiser());

  public static Function<EntityManager, Repository.List<Inventory>> inventoryList = (em) -> HibernateRepository.list(Inventory.class, em);
}

