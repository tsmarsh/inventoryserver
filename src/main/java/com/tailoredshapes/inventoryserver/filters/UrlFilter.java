package com.tailoredshapes.inventoryserver.filters;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

/**
 * Created by tmarsh on 2/18/14.
 */
public class UrlFilter extends Filter{
    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        chain.doFilter(httpExchange);
    }

    @Override
    public String description() {
        return "Extracts data from the url";
    }
}
