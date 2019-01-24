package com.pl2_vertx.controller;

import com.pl2_vertx.service.EmployeeService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;

public class EntryPoint extends AbstractVerticle {


    @Override
    public void start(Future<Void> fut) {
        // Create a router object.
        Vertx vertx = Vertx.vertx();
        HttpServer hs = vertx.createHttpServer();
        Router router = Router.router(vertx);


        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>Hello from my first Vert.x 3 application</h1>");
        });

        router.get("/api/employees").handler(EmployeeController::getAll);
        router.get("/api/employees/:id").handler(EmployeeController::getOne);

        router.post("/api/employees").handler(EmployeeController::addOne);

        router.delete("/api/employees/:id").handler(EmployeeController::deleteOne);

        router.patch("/api/employees/:id").handler(EmployeeController::updateOne);

        hs.requestHandler(router)
                .listen(8080);

    }



}
