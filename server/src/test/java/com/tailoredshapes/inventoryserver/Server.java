package com.tailoredshapes.inventoryserver;


import static spark.Spark.port;

public class Server {
    static boolean started = false;

    public static void start(){
        if(!started){
            started = true;
            port(5555);
            Router.route(TestPersistence.emf);
        }
    }
}
