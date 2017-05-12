package com.tailoredshapes.inventoryserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tailoredshapes.inventoryserver.dao.CategorySaver;
import com.tailoredshapes.inventoryserver.dao.ChildFreeSaver;
import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.dao.InventorySaver;
import com.tailoredshapes.inventoryserver.dao.MetricSaver;
import com.tailoredshapes.inventoryserver.dao.UserSaver;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.Category;
import com.tailoredshapes.inventoryserver.model.Inventory;
import com.tailoredshapes.inventoryserver.model.Metric;
import com.tailoredshapes.inventoryserver.model.MetricType;
import com.tailoredshapes.inventoryserver.model.User;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryRepository;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;

import static com.tailoredshapes.inventoryserver.encoders.Encoders.shaEncoder;
import static com.tailoredshapes.inventoryserver.extractors.Extractors.inventoryExtractor;
import static com.tailoredshapes.inventoryserver.extractors.Extractors.userIdExtractor;
import static com.tailoredshapes.inventoryserver.repositories.memory.InMemoryLookers.categoryByFullName;
import static com.tailoredshapes.inventoryserver.repositories.memory.InMemoryLookers.metricTypeByName;
import static com.tailoredshapes.inventoryserver.repositories.memory.InMemoryRepository.findBy;
import static com.tailoredshapes.inventoryserver.repositories.memory.InMemoryRepository.findById;
import static com.tailoredshapes.inventoryserver.serialisers.Serialisers.inventorySerializerBuilder;
import static com.tailoredshapes.inventoryserver.serialisers.Serialisers.metricSerialiser;
import static com.tailoredshapes.inventoryserver.serialisers.Serialisers.userSerializerBuilder;

public interface InMemoryProviders {

  Map<Long, MetricType> metricTypeDB = new ConcurrentHashMap<>();
  Map<Long, Metric> metricDB = new ConcurrentHashMap<>();
  Map<Long, Category> categoryDB = new ConcurrentHashMap<>();
  Map<Long, Inventory> inventoryDB = new ConcurrentHashMap<>();
  Map<Long, User> userDB = new ConcurrentHashMap<>();

  DAO<MetricType> metricTypeDAO = new InMemoryDAO<>(metricTypeDB, shaEncoder, new ChildFreeSaver<>());
  DAO<Metric> metricDAO = new InMemoryDAO<>(metricDB, shaEncoder, new MetricSaver(metricTypeDAO));
  DAO<Category> categoryDAO = new InMemoryDAO<>(categoryDB, shaEncoder, new CategorySaver(findBy(categoryDB), categoryByFullName));
  DAO<Inventory> inventoryDAO = new InMemoryDAO<>(inventoryDB, shaEncoder, new InventorySaver(metricDAO, categoryDAO));
  DAO<User> userDAO = new InMemoryDAO<>(userDB, shaEncoder, new UserSaver(inventoryDAO));



  Parser<Inventory> inventoryParser = InventoryParser.inventoryParser(
    findBy(categoryDB),
    findById(inventoryDB),
    findBy(metricTypeDB),
    categoryByFullName,
    metricTypeByName,
    inventoryExtractor);

  Parser<User> userParser = UserParser.userParser(findById(userDB), inventoryParser, userIdExtractor);

  UrlBuilder<Inventory> inventoryUrlBuilder =
    new InventoryUrlBuilder(Environment.protocol, Environment.host, Environment.port);

  UrlBuilder<User> userUrlBuilder = new UserUrlBuilder(Environment.protocol, Environment.host, Environment.port);

  Serialiser<Inventory> inventorySerialiser =
    inventorySerializerBuilder.apply(inventoryUrlBuilder, metricSerialiser);

  Serialiser<User> userSerialiser = userSerializerBuilder.apply(userUrlBuilder, inventorySerialiser);

  Repository.List<Inventory> inventoryList = InMemoryRepository.list(inventoryDB);
  Repository.List<User> userList = InMemoryRepository.list(userDB);
  Repository.FindById<Inventory> inventoryById = InMemoryRepository.findById(inventoryDB);
  Repository.FindById<User> userById = InMemoryRepository.findById(userDB);
  Repository.FindBy<User, Map<Long, User>> findUserBy = InMemoryRepository.findBy(userDB);
}

