package com.tailoredshapes.inventoryserver;

import java.util.logging.Logger;

import org.eclipse.jetty.server.CustomRequestLog;
import org.eclipse.jetty.server.RequestLog;
import spark.embeddedserver.EmbeddedServers;
import spark.embeddedserver.jetty.EmbeddedJettyFactory;

public class SparkUtils {
    public static void createServerWithRequestLog(Logger logger) {
        EmbeddedJettyFactory factory = createEmbeddedJettyFactoryWithRequestLog(logger);
        EmbeddedServers.add(EmbeddedServers.Identifiers.JETTY, factory);
    }

    private static EmbeddedJettyFactory createEmbeddedJettyFactoryWithRequestLog(Logger logger) {
        RequestLog requestLog = new CustomRequestLog();
        return new EmbeddedJettyFactoryConstructor(requestLog).create();
    }
}