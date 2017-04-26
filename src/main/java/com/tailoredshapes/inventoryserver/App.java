package com.tailoredshapes.inventoryserver;

import java.net.URL;
import java.util.function.Function;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.filters.ParamToValue;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.Looker;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.repositories.hibernate.HibernateRepository;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.validators.Validator;

import javax.persistence.EntityManager;

import static com.tailoredshapes.inventoryserver.Persistence.persistent;
import static com.tailoredshapes.inventoryserver.Persistence.transactional;
import static com.tailoredshapes.inventoryserver.Pestlet.getlet;
import static com.tailoredshapes.inventoryserver.Pestlet.postlet;
import static com.tailoredshapes.inventoryserver.ProdPersistence.emf;
import static com.tailoredshapes.inventoryserver.filters.ParamToValue.valueExtractor;
import static spark.Spark.get;
import static spark.Spark.post;

public class App {

//  public <T extends Idable, S> void pestlet(
//    String path,
//
//    Function<EntityManager, DAO<T>> daoBuilder,
//    Function<EntityManager, Repository<T, EntityManager>> repositoryBuilder,
//    UrlBuilder<T> urlBuilder,
//    String parameterName,
//    Validator<T> validator,
//    Looker<S, T, EntityManager> looker,
//    Parser<T> parser,
//
//    IdExtractor<S> idExtractor) {
//
//    get(path, (req, res) ->
//      persistent(emf, (em) -> {
//        Repository<T, EntityManager> repository = repositoryBuilder.apply(em);
//        return valueExtractor(parser, idExtractor, looker, repository, parameterName, req, res,
//                       getlet(validator, urlBuilder, req, res, repository));
//      }
//
//    ));
//
//    post(path, (req, res) ->
//      transactional(emf, (em) ->{
//        Repository<T, EntityManager> repository = repositoryBuilder.apply(em);
//        DAO<T> dao = daoBuilder.apply(em);
//        return valueExtractor(parser, idExtractor, looker, repository, parameterName, req, res, postlet(dao, urlBuilder, res));
//      }
//    ));
//  }

  public static void main(String... args) {

  }
}
