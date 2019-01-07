package com.golden.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {

        Vertx vertex= Vertx.vertx();

        HttpServer hs = vertex.createHttpServer();

        Router router = Router.router(vertex);

        router.route("/hi")
                .handler(routingContext -> {
            HttpServerResponse rsp =routingContext.response();

            rsp.putHeader("content-type","application/json")
                    .end("hello");
                    });


        hs.requestHandler(router)
                .listen(8100);


    }
}
