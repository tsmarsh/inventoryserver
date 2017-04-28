package com.tailoredshapes.inventoryserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;

public class Server {
    static boolean started = false;
    static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void start(){
        Providers.port = 5555;
        logger.info("Starting test server on port " + Providers.port);
        if(!started){
            started = true;
            port(Providers.port);
            Router.route(TestPersistence.emf);
        }
    }
}
