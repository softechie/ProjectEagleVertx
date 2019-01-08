package controller;

import DAO.EmployeeService;
import dto.Employee;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class EmployeeController extends AbstractVerticle {

    private static EmployeeService empService = EmployeeService.getService();

    @Override
    public void start(Future<Void> fut) {
        // Create a router object.
        Vertx vertx = Vertx.vertx();
        HttpServer hs = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());


        router. route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>this is the / end point</h1>");
        });

        router.get("/api/employees").handler(this::getAll);
        router.get("/api/employees/:id").handler(this::getOne);

        router.post("/api/employees").handler(this::addOne);

        router.delete("/api/employees/:id").handler(this::deleteOne);

        router.patch("/api/employees/:id").handler(this::updateOne);

        hs.requestHandler(router)
                .listen(8300);

    }


    private  void getAll(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(empService.getAllEmployees().values()));
    }

    private  void addOne(RoutingContext routingContext) {

        JsonObject json = routingContext.getBodyAsJson();
        Employee emp = new Employee();
        emp.setName(json.getString("name"));
        emp.setEmail(json.getString("email"));
        emp.setPhone(json.getString("phone"));
        emp.setTenure(json.getString("tenure"));
        empService.addEmployee(emp);
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(emp));

    }
    private  void getOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if(id == null){
            routingContext.response().setStatusCode(400).end();

        }else{
            Employee emp =  empService.getOneEmployee(id);
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(emp));

        }

    }
    private  void deleteOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if(id == null){
            routingContext.response().setStatusCode(400).end();

        }else{
           empService.removeEmployee(id);
            routingContext.response().setStatusCode(200).end();

        }

    }
    private  void updateOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if(id == null){
            routingContext.response().setStatusCode(400).end();

        }else{

         Employee emp =   empService.getOneEmployee(id);
         if (emp == null){
             routingContext.response().setStatusCode(400).end();

         }
            JsonObject json = routingContext.getBodyAsJson();
            emp.setName(json.getString("name"));
            emp.setEmail(json.getString("email"));
            emp.setPhone(json.getString("phone"));
            emp.setTenure(json.getString("tenure"));
            empService.updateEmployee(emp);
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(emp));

        }

    }
}
