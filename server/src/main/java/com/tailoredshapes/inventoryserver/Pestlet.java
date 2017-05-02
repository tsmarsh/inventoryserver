package com.tailoredshapes.inventoryserver;

import java.net.URL;
import java.util.function.Function;

import com.tailoredshapes.inventoryserver.dao.DAO;
import com.tailoredshapes.inventoryserver.model.Idable;
import com.tailoredshapes.inventoryserver.repositories.Repository;
import com.tailoredshapes.inventoryserver.urlbuilders.UrlBuilder;
import com.tailoredshapes.inventoryserver.validators.Validator;

import spark.Request;
import spark.Response;

import static com.tailoredshapes.underbar.Die.rethrow;


public interface Pestlet {

  static <T> Function<T, Object> getlet(Validator<T> validator,
                                        UrlBuilder<T> urlBuilder,
                                        Request req,
                                        Response res,
                                        Repository.List<T> repository) {
    return (t) -> {
      if (validator.validate(t)) {
        String tUrl = urlBuilder.build(t);
        URL url = rethrow(() -> new URL(tUrl), () -> String.format("%s not a valid url", tUrl));

        if (!url.getPath().equals(req.uri())) {
          res.redirect(tUrl);
        }

        return t;
      } else {
        return repository.list();
      }
    };
  }

  static <T extends Idable> Function<T, Object> postlet(DAO<T> dao, UrlBuilder<T> urlBuilder, Response res) {
    return (t) -> {
      T tPrime;
      if (t.getId() == null) {
        tPrime = dao.create(t);
      } else {
        tPrime = dao.update(t);
      }

      res.redirect(urlBuilder.build(tPrime));
      return tPrime;
    };
  }
}
