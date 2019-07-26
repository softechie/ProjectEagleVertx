package com.pl2_vertx.controller;

import com.pl2_vertx.dto.Employee;
import com.pl2_vertx.dto.Log;
import com.pl2_vertx.producer.Producer;
import com.pl2_vertx.service.TestService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import com.pl2_vertx.service.EmployeeService;
import java.util.function.Function;
import java.util.function.Predicate;

public class EmployeeController {

    //private static TestService empService = new TestService();
    private static EmployeeService empService = new EmployeeService();
    private static Producer producer = new Producer();

    public static void addOne(RoutingContext routingContext) {
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

        producer.sendLog(new Log("Employee Added."));
    }

    public static void getOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if(id == null){
            routingContext.response().setStatusCode(400).end();
        }else{
            Employee emp = empService.getOneEmployee(id);
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(emp));

            producer.sendLog(new Log("Employee Retrieved."));
        }
    }

    public static void getAll(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(empService.getAllEmployees().values()));

        producer.sendLog(new Log("All Employees Retrieved."));
    }

    // Get a list of column values from employee table
    public static void getListOfColValues(RoutingContext routingContext, Function<Employee,String> lambda) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(empService.getListOfColValues(lambda)));

        producer.sendLog(new Log("Retrieve Column Values for All Employees."));
    }

    // Get an employee by column value
    public static void getEmployeeByCol(RoutingContext routingContext, Predicate<Employee> pred) {
        Employee emp = empService.getEmployeeByCol(pred);
        if(emp == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(Json.encodePrettily(emp));
        }

        producer.sendLog(new Log("Employee Retrieved by Column."));
    }

    // Get list of sorted employees
    public static void getSortedEmployees(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(empService.getSortedEmployees().values()));

        producer.sendLog(new Log("All Employees Retrieved Sorted by Name."));
    }

    public static void removeOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if(id == null){
            routingContext.response().setStatusCode(400).end();
        }else{
            empService.removeEmployee(id);
            routingContext.response().setStatusCode(200).end();
            producer.sendLog(new Log("Employee Removed."));
        }

    }

    // Delete all
    public static void removeAll(RoutingContext routingContext) {
        empService.removeAllEmployees();
        routingContext.response().setStatusCode(200).end();
        producer.sendLog(new Log("Remove All Employees."));
    }

    public static void updateOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if(id == null){
            routingContext.response().setStatusCode(400).end();
        }else{
            Employee emp = empService.getOneEmployee(id);
            if (emp == null){
                routingContext.response().setStatusCode(400).end();
                return;
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

            producer.sendLog(new Log("Employee Updated."));
        }

    }
}