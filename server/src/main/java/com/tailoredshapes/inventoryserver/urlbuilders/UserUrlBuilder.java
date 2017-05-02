package com.tailoredshapes.inventoryserver.urlbuilders;

import java.net.MalformedURLException;
import java.net.URL;

import com.tailoredshapes.inventoryserver.model.User;

public class UserUrlBuilder implements UrlBuilder<User> {


  private final String protocol;
  private final String host;
  private final int port;

  public UserUrlBuilder(String protocol,
                        String host,
                        int port) {
    this.protocol = protocol;
    this.host = host;
    this.port = port;
  }

  @Override
  public String build(User user) {
    try {

      return user.getId() != null ?
             new URL(protocol, host, port, String.format("/users/%s/%d", user.getName(), user.getId())).toString()
                                  : null;
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
