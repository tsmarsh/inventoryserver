package com.tailoredshapes.inventoryserver;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tailoredshapes.inventoryserver.handlers.InventoryHandler;

import javax.inject.Named;
import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by tmarsh on 2/14/14.
 */
public class InventoryModule implements Module {
    private final String host;
    private final int port;

    public InventoryModule(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void configure(Binder binder) {
        binder.bind(HttpHandler.class).to(InventoryHandler.class);
    }

    @Provides
    @Named("host")
    public String hostProvider(){
        return host;
    }

    @Provides
    @Named("port")
    public Integer portProvider(){
        return port;
    }

    @Provides
    public HttpServer httpServerProvider(@Named("host") String host, @Named("port") Integer port, HttpHandler handler){
        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), -1);
            httpServer.createContext("/", handler);
            return httpServer;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
