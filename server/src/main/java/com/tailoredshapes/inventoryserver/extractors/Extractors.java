package com.tailoredshapes.inventoryserver.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Extractors {

  Pattern inventoryIdPattern = Pattern.compile("^.*/inventories/?(-?\\d+)");
  Pattern userIdPattern = Pattern.compile("/users/\\w+/?(-?\\d+)?/?.*$");

  IdExtractor<Long> inventoryExtractor = (path) -> {
    Matcher matcher = inventoryIdPattern.matcher(path);
    if (matcher.matches()) {
      return Long.parseLong(matcher.group(1));
    } else {
      return null;
    }
  };

  IdExtractor<Long> userIdExtractor = (path) -> {
    Matcher matcher = userIdPattern.matcher(path);
    Long id = null;
    if (matcher.matches()) {
      id = Long.parseLong(matcher.group(1));
    }

    return id;
  };
}

