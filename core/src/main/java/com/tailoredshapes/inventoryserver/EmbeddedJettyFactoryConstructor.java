package com.tailoredshapes.inventoryserver;

import org.eclipse.jetty.server.RequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import spark.embeddedserver.jetty.EmbeddedJettyFactory;
import spark.embeddedserver.jetty.JettyServerFactory;

public class EmbeddedJettyFactoryConstructor {
    RequestLog requestLog;

    public EmbeddedJettyFactoryConstructor(RequestLog requestLog) {
        this.requestLog = requestLog;
    }

    EmbeddedJettyFactory create() {

        return new EmbeddedJettyFactory(new JettyServerFactory() {
            @Override
            public Server create(int maxThreads, int minThreads, int threadTimeoutMillis) {
                Server server;
                if (maxThreads > 0) {
                    int max = maxThreads > 0 ? maxThreads : 200;
                    int min = minThreads > 0 ? minThreads : 8;
                    int idleTimeout = threadTimeoutMillis > 0 ? threadTimeoutMillis : '\uea60';
                    server = new Server(new QueuedThreadPool(max, min, idleTimeout));
                } else {
                    server = new Server();
                }

                server.setRequestLog(requestLog);
                return server;
            }

            @Override
            public Server create(ThreadPool threadPool) {
                return new Server(threadPool);
            }
        });
    }
}
