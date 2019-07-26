package com.pl2_vertx.controller;

import com.pl2_vertx.dto.Employee;
import com.pl2_vertx.dto.Log;
import com.pl2_vertx.producer.Producer;
import com.pl2_vertx.service.PostgresEmployeeService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.function.Function;
import java.util.function.Predicate;

public class PostgresEmployeeController {

    private static PostgresEmployeeService empService = new PostgresEmployeeService();
    private static Producer producer = new Producer();

    public static void addOne(RoutingContext routingContext) {
        JsonObject json = routingContext.getBodyAsJson();
        Employee emp = new Employee();
        String name = json.getString("name");
        String email = json.getString("email");
        String phone = json.getString("phone");
        String tenure = json.getString("tenure");

        if(name == null || email == null
                || phone == null || tenure == null ) {

            routingContext.response().setStatusCode(400).end();
        } else {
            emp.setName(name);
            emp.setEmail(email);
            emp.setPhone(phone);
            emp.setTenure(tenure);

            empService.addEmployee(emp, employeeAsyncResult -> {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(employeeAsyncResult.result()));

                producer.sendLog(new Log("Employee Added."));
            });
        }
    }

    public static void getOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if(id == null){
            routingContext.response().setStatusCode(400).end();
        }else{
            empService.getOneEmployee(id, employeeAsyncResult -> {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(employeeAsyncResult.result()));

                producer.sendLog(new Log("One Employees Retrieved."));
            });
        }
    }

    public static void getAll(RoutingContext routingContext) {
        empService.getAllEmployees(employeeAsyncResult -> {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(employeeAsyncResult.result().values()));

            producer.sendLog(new Log("All Employees Retrieved."));
        });
    }

    // Get a list of column values from employee table
    public static void getListOfColValues(RoutingContext routingContext, Function<Employee,String> lambda) {
        empService.getListOfColValues(lambda, employeeAsyncResult -> {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(employeeAsyncResult.result()));

            producer.sendLog(new Log("Retrieve Column Values for All Employees."));
        });
    }

    // Get an employee by column value
    public static void getEmployeeByCol(RoutingContext routingContext, Predicate<Employee> pred) {
        empService.getEmployeeByCol(pred, employeeAsyncResult -> {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(employeeAsyncResult.result()));

            producer.sendLog(new Log("Employee Retrieved by Column."));
        });
    }

    // Get list of sorted employees
    public static void getSortedEmployees(RoutingContext routingContext) {
        empService.getSortedEmployees(employeeAsyncResult -> {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(employeeAsyncResult.result()));

            producer.sendLog(new Log("All Employees Retrieved Sorted by Name."));
        });
    }

    public static void removeOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if(id == null){
            routingContext.response().setStatusCode(400).end();
        }else{
            empService.removeEmployee(id);
            producer.sendLog(new Log("Employee Removed."));
        }

    }

    // Delete all
    public static void removeAll(RoutingContext routingContext) {
        empService.removeAllEmployees();
        producer.sendLog(new Log("Remove All Employees."));
    }

    public static void updateOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");

        if(id == null){
            routingContext.response().setStatusCode(400).end();
        }else{
            empService.getOneEmployee(id, employeeAsyncResult -> {
                if (employeeAsyncResult == null){
                    routingContext.response().setStatusCode(400).end();
                    return;
                }
                Employee emp = employeeAsyncResult.result();
                JsonObject json = routingContext.getBodyAsJson();
                emp.setName(json.getString("name"));
                emp.setEmail(json.getString("email"));
                emp.setPhone(json.getString("phone"));
                emp.setTenure(json.getString("tenure"));
                empService.updateEmployee(emp);
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(emp));

            });
            producer.sendLog(new Log("Employee Updated."));
        }

    }

}
