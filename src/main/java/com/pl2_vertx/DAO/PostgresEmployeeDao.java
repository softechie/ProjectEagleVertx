package com.pl2_vertx.DAO;

import com.pl2_vertx.config.DbConfig;
import com.pl2_vertx.dto.Employee;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.StringEscapeUtils;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLClient;
import io.vertx.ext.web.RoutingContext;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class PostgresEmployeeDao {
    private static PostgresEmployeeDao empDao;
    private JsonObject postgreSQLClientConfig;
    private SQLClient postgreSQLClient;

    private PostgresEmployeeDao(){
        postgreSQLClientConfig = new JsonObject().put("host", "localhost")
                .put("port", 5433)
                .put("username", "postgres")
                .put("password", "password")
                .put("database", "postgres");

        postgreSQLClient = PostgreSQLClient.createShared(Vertx.currentContext().owner(),
                postgreSQLClientConfig);
    }

    public static PostgresEmployeeDao getService(){
        if(empDao == null){
            empDao = new PostgresEmployeeDao();
        }
        return empDao;
    }

    public void addEmployee(Employee emp){
        JsonObject jsonEmp = JsonObject.mapFrom(emp);
        JsonArray args = new JsonArray().add(emp.getEmpId()).add(jsonEmp.toString());
       // String query = "INSERT INTO public.\"Employees\" (empid, employee) VALUES (?, ?)";
        
        String query = "INSERT INTO employees (empid, employee) VALUES (?, ?)";

        postgreSQLClient.queryWithParams(query, args, resultSetAsync -> {
            /*if(resultSetAsync.succeeded()) {
                handler.handle(Future.succeededFuture(emp));
            } else {
                handler.handle(Future.failedFuture("Add Employee Failed."));
                System.out.println("Add Employee Failed. CAUSE: " + resultSetAsync.cause());}*/
            
        });
    }

    public void getOneEmployee(String id, Handler<AsyncResult<Employee>> handler){
        JsonArray args = new JsonArray().add(id);
        String query = "SELECT employee FROM public.\"Employees\" WHERE empid = ?";

        postgreSQLClient.queryWithParams(query, args, resultSetAsync -> {
            List<Employee> list = resultSetAsync.result().getRows().stream()
                    .map(e -> e.getString("employee"))
                    .map(e -> Json.decodeValue(e, Employee.class))
                    .collect(Collectors.toList());

            if(list.size() > 0)
                handler.handle(Future.succeededFuture(list.get(0)));
            else
                handler.handle(Future.succeededFuture(null));
        });
    }

    public void getAllEmployees(Handler<AsyncResult<Map<String, Employee>>> handler) {
        postgreSQLClient.query("SELECT employee FROM public.\"Employees\"", resultSetAsync -> {
            Map<String, Employee> empMap = resultSetAsync.result().getRows()
                    .stream()
                    .map(e -> e.getString("employee"))
                    .map(e -> Json.decodeValue(e, Employee.class))
                    .collect(Collectors.toMap(e->e.getEmpId(), e->e));

            handler.handle(Future.succeededFuture(empMap));
        });
    }


    public void getSortedEmployees(Handler<AsyncResult<Map<String, Employee>>> handler){
        postgreSQLClient.query("SELECT employee FROM public.\"Employees\"", resultSetAsync -> {
            Map<String, Employee> empMap = resultSetAsync.result().getRows()
                    .stream()
                    .map(e -> e.getString("employee"))
                    .map(e -> Json.decodeValue(e, Employee.class))
                    .sorted(Comparator.comparing(Employee::getName))
                    .collect(Collectors.toMap(e->e.getEmpId(), e->e, (e1,e2) -> e1, LinkedHashMap::new));

            handler.handle(Future.succeededFuture(empMap));
        });
    }

    public void getEmployeeByCol(Predicate<Employee> pred, Handler<AsyncResult<List<Employee>>> handler){
        postgreSQLClient.query("SELECT employee FROM public.\"Employees\"", resultSetAsync -> {
            List<Employee> list = resultSetAsync.result().getRows().stream()
                    .map(e -> e.getString("employee"))
                    .map(e -> Json.decodeValue(e, Employee.class))
                    .filter(pred)
                    .collect(Collectors.toList());

            handler.handle(Future.succeededFuture(list));
        });
    }

    public void getListOfColValues(Function<Employee, String> lambda, Handler<AsyncResult<List<String>>> handler){
        postgreSQLClient.query("SELECT employee FROM public.\"Employees\"", resultSetAsync -> {
            List<String> list = resultSetAsync.result().getRows()
                    .stream()
                    .map(e -> e.getString("employee"))
                    .map(e -> Json.decodeValue(e, Employee.class))
                    .map(lambda)
                    .collect(Collectors.toList());

            handler.handle(Future.succeededFuture(list));
        });
    }

    public void removeEmployee(String id){
        JsonArray args = new JsonArray().add(id);
        String query = "DELETE FROM public.\"Employees\" WHERE empid = ?";
        postgreSQLClient.queryWithParams(query, args, resultSetAsync->{});
    }

    public void removeAllEmployees(){
        String query = "DELETE FROM public.\"Employees\"";
        postgreSQLClient.query(query, resultSetAsync ->{});
    }

    public void updateEmployee(Employee emp){
        JsonObject jsonEmp = JsonObject.mapFrom(emp);
        JsonArray args = new JsonArray().add(jsonEmp.toString()).add(emp.getEmpId());
        postgreSQLClient.updateWithParams("UPDATE public.\"Employees\" SET employee = ? WHERE empid = ?", args, resultSetAsync -> {});
    }

}

