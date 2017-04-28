package com.tailoredshapes.inventoryserver;

import com.tailoredshapes.inventoryserver.validators.Environment;

import static spark.Spark.get;
import static spark.Spark.port;
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
    port(Environment.port);
    Router.route(ProdPersistence.emf);
  }
}
