package com.tailoredshapes.inventoryserver;

import java.util.function.Function;

import com.tailoredshapes.inventoryserver.dao.CategorySaver;
import com.tailoredshapes.inventoryserver.dao.ChildFreeSaver;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InventorySaver;
import com.tailoredshapes.inventoryserver.dao.MetricSaver;
import com.tailoredshapes.inventoryserver.dao.UserSaver;
import com.tailoredshapes.inventoryserver.dao.hibernate.HibernateDAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;
import com.tailoredshapes.inventoryserver.serialisers.InventoryStringSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.MetricStringSerialiser;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.serialisers.UserStringSerialiser;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;
import com.tailoredshapes.inventoryserver.validators.Environment;

import javax.persistence.EntityManager;

import static com.tailoredshapes.inventoryserver.encoders.Encoders.shaEncoder;
import static com.tailoredshapes.inventoryserver.extractors.Extractors.inventoryExtractor;
import static com.tailoredshapes.inventoryserver.extractors.Extractors.userIdExtractor;
import static com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateLookers.catergoryByFullName;
import static com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateLookers.metricTypeByName;
import static com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository.findBy;
import static com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository.findById;

public interface Providers {

  DAOProvider<MetricType> metricTypeDAO = (em) ->
    new HibernateDAO<>(MetricType.class, em, new ChildFreeSaver<>(), shaEncoder);
  DAOProvider<Metric> metricDAO = (em) ->
    new HibernateDAO<>(Metric.class, em, new MetricSaver(metricTypeDAO.apply(em)), shaEncoder);
  DAOProvider<Category> categoryDAO = (em) ->
    new HibernateDAO<>(
      Category.class,
      em,
      new CategorySaver<>(findBy(em), catergoryByFullName),
      shaEncoder);
  DAOProvider<Inventory> inventoryDAO = (em) ->
    new HibernateDAO<>(Inventory.class,
                       em,
                       new InventorySaver(metricDAO.apply(em), categoryDAO.apply(em)),
                       shaEncoder);
  DAOProvider<User> userDAO =
    (em) -> new HibernateDAO<>(User.class, em, new UserSaver(inventoryDAO.apply(em)), shaEncoder);
  ParserProvider<Inventory> inventoryParser = (em) -> InventoryParser.inventoryParser(
    findBy(em),
    findById(Inventory.class, em),
    findBy(em),
    catergoryByFullName,
    metricTypeByName,
    inventoryExtractor);
  ParserProvider<User> userParser =
    (em) -> UserParser.userParser(findById(User.class, em), inventoryParser.apply(em), userIdExtractor);
  UrlBuilder<Inventory> inventoryUrlBuilder =
    new InventoryUrlBuilder(Environment.protocol, Environment.host, Environment.port);
  UrlBuilder<User> userUrlBuilder = new UserUrlBuilder(Environment.protocol, Environment.host, Environment.port);
  Serialiser<Inventory> inventorySerialiser =
    new InventoryStringSerialiser(inventoryUrlBuilder, new MetricStringSerialiser());
  Serialiser<User> userSerialiser = new UserStringSerialiser(userUrlBuilder, inventorySerialiser);
  Function<EntityManager, Repository.List<Inventory>> inventoryList =
    (em) -> HibernateRepository.list(Inventory.class, em);
  Function<EntityManager, Repository.List<User>> userList = (em) -> HibernateRepository.list(User.class, em);

  interface DAOProvider<T> extends Function<EntityManager, DAO<T>> {}
  interface ParserProvider<T> extends Function<EntityManager, Parser<T>> {}
}

