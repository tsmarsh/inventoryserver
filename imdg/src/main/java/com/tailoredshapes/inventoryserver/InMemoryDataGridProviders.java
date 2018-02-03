package com.tailoredshapes.inventoryserver;

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.tailoredshapes.inventoryserver.dao.*;
import com.tailoredshapes.inventoryserver.dao.memory.InMemoryDAO;
import com.tailoredshapes.inventoryserver.model.*;
import com.tailoredshapes.inventoryserver.parsers.InventoryParser;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.parsers.UserParser;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.memory.InMemoryRepository;
import com.tailoredshapes.inventoryserver.serialisers.Serialiser;
import com.tailoredshapes.inventoryserver.serialisers.Serialisers;
import com.tailoredshapes.inventoryserver.serialisers.hazelcast.HazelcastSerialisers;
import com.tailoredshapes.inventoryserver.urlbuilders.InventoryUrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.urlbuilders.UserUrlBuilder;

import java.util.Arrays;
import java.util.Map;

import static com.tailoredshapes.inventoryserver.encoders.Encoders.shaEncoder;
import static com.tailoredshapes.inventoryserver.extractors.Extractors.inventoryExtractor;
import static com.tailoredshapes.inventoryserver.extractors.Extractors.userIdExtractor;
import static com.tailoredshapes.inventoryserver.repositories.memory.InMemoryLookers.categoryByFullName;
import static com.tailoredshapes.inventoryserver.repositories.memory.InMemoryLookers.metricTypeByName;
import static com.tailoredshapes.inventoryserver.repositories.memory.InMemoryRepository.findBy;
import static com.tailoredshapes.inventoryserver.repositories.memory.InMemoryRepository.findById;
import static com.tailoredshapes.inventoryserver.serialisers.Serialisers.*;

public interface InMemoryDataGridProviders {

  static Config buildConfig(){
    Config config = new Config();
    config.getSerializationConfig()
            .addSerializerConfig(
                    new SerializerConfig()
                      .setTypeClass(User.class).setImplementation(HazelcastSerialisers.userSerializer))
            .addSerializerConfig(
                    new SerializerConfig()
                            .setTypeClass(Category.class).setImplementation(HazelcastSerialisers.categorySerialiser))
            .addSerializerConfig(
                    new SerializerConfig()
                            .setTypeClass(Inventory.class).setImplementation(HazelcastSerialisers.inventorySerializer))
            .addSerializerConfig(
                    new SerializerConfig()
                            .setTypeClass(Metric.class).setImplementation(HazelcastSerialisers.metricSerialiser))
            .addSerializerConfig(
                    new SerializerConfig()
                            .setTypeClass(MetricType.class).setImplementation(HazelcastSerialisers.metricTypeSerialiser));
    return config;
  }

  Config cfg = buildConfig();

  HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);

  Map<Long, MetricType> metricTypeDB = instance.getMap("metricType");
  Map<Long, Metric> metricDB = instance.getMap("metric");
  Map<Long, Category> categoryDB = instance.getMap("category");
  Map<Long, Inventory> inventoryDB = instance.getMap("inventory");
  Map<Long, User> userDB = instance.getMap("user");

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

