package com.tailoredshapes.inventoryserver.filters;

import java.util.function.Function;

import com.tailoredshapes.inventoryserver.extractors.IdExtractor;
import com.tailoredshapes.inventoryserver.parsers.Parser;
import com.tailoredshapes.inventoryserver.repositories.Looker;
import com.tailoredshapes.inventoryserver.repositories.Repository;

import spark.Request;

import static com.tailoredshapes.underbar.Die.dieIf;

public interface ParamToValue {
  static <S, T, U, X> X valueExtractor(Parser<T> parser,
                                       IdExtractor<S> extractor,
                                       Looker<S, T, U> looker,
                                       Repository.FindBy<T, U> repository,
                                       String parameterName,
                                       Request req,
                                       Function<T, X> next) {
    T t = null;
    if (req.params().containsKey(parameterName)) {
      String tJson = req.params(parameterName);
      t = parser.parse(tJson);
    } else {
      S extract = extractor.extract(req.uri());
      if (extract != null) {

        t = repository.findBy(looker.lookFor(extract));
        dieIf(t == null, () -> String.format("No value with id %s", extract));
      }
    }

    return next.apply(t);
  }
}
