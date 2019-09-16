package com.pl2_vertx.controller;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;


public class PostgresEntryPoint extends AbstractVerticle {
    @Override
    public void start(Future<Void> fut) {
        // Create a router object.
        HttpServer hs = vertx.createHttpServer();
        Router router = Router.router(vertx);
        PostgresEmployeeController ec = new PostgresEmployeeController();
        //Allows the usage of body in http requests.
        router.route().handler(BodyHandler.create());

        // Handler for search urls
        Handler<RoutingContext> rcSearch = routingContext -> {
            String searchStr = routingContext.pathParams()
                    .values()
                    .stream().findFirst().get();
            String paramName = routingContext.pathParams()
                    .keySet()
                    .stream().findFirst().get();

            if(searchStr == null) {
                routingContext.response().setStatusCode(400).end();
            } else {
                switch (paramName) {
                    case "email":
                        ec.getEmployeeByCol(routingContext, x -> x.getEmail().equals(searchStr));
                        break;
                    case "empid":
                        ec.getEmployeeByCol(routingContext, x -> x.getEmpId().equals(searchStr));
                        break;
                    case "phone":
                        ec.getEmployeeByCol(routingContext, x -> x.getPhone().equals(searchStr));
                        break;
                    default:
                        routingContext.response().setStatusCode(400).end();
                }
            }

        };

        // Handler for read urls
        Handler<RoutingContext> rcRead = routingContext -> {
            // Get last directory in path
            String paramName = routingContext.currentRoute().getPath();
            String aux[] = paramName.split("/");
            paramName = aux[aux.length-1];

            switch (paramName) {
                case "names":
                    ec.getListOfColValues(routingContext, x->x.getName());
                    break;
                case "emails":
                    ec.getListOfColValues(routingContext, x->x.getEmail());
                    break;
                case "phones":
                    ec.getListOfColValues(routingContext, x->x.getPhone());
                    break;
                case "wls":
                    ec.getListOfColValues(routingContext, x->x.getWl());
                    break;
                default:
                    routingContext.response().setStatusCode(400).end();
            }

        };

        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>Hello from my first Vert.x 3 application</h1>");
        });

        router.get("/api/employees").handler(PostgresEmployeeController::getAll);
        router.get("/api/employees/:id").handler(PostgresEmployeeController::getOne);
        router.get("/api/employees/read/names").handler(rcRead);
        router.get("/api/employees/read/emails").handler(rcRead);
        router.get("/api/employees/read/phones").handler(rcRead);
        router.get("/api/employees/read/wls").handler(rcRead);
        router.get("/api/employees/search/email/:email").handler(rcSearch);
        router.get("/api/employees/search/empid/:empid").handler(rcSearch);
        router.get("/api/employees/search/phone/:phone").handler(rcSearch);
        router.get("/api/employees/read/sorted").handler(PostgresEmployeeController::getSortedEmployees);

        router.post("/api/employees").handler(PostgresEmployeeController::addOne);
        router.post("/api/employees/initializeData").handler(PostgresEmployeeController::addAll);
        
        router.delete("/api/employees/:id").handler(PostgresEmployeeController::removeOne);
        router.delete("/api/employees/delete/all").handler(PostgresEmployeeController::removeAll);

        router.patch("/api/employees/:id").handler(PostgresEmployeeController::updateOne);
        hs.requestHandler(router)
                .listen(8080);
    }



}
