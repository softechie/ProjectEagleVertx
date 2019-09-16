package com.pl2_vertx.service;

import com.pl2_vertx.DAO.AsyncCBEmployeeDao;
import com.pl2_vertx.DAO.PostgresEmployeeDao;
import com.pl2_vertx.dto.Employee;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

// singelton
public class PostgresEmployeeService {

    private PostgresEmployeeDao employeeDao = PostgresEmployeeDao.getService();

   // public void addEmployee(Employee emp, Handler<AsyncResult<Employee>> handler){employeeDao.addEmployee(emp, handler);}
    public void getOneEmployee(String id, Handler<AsyncResult<Employee>> handler){employeeDao.getOneEmployee(id, handler);}
    public void getAllEmployees(Handler<AsyncResult<Map<String, Employee>>> handler){employeeDao.getAllEmployees(handler);}
    public void getEmployeeByCol(Predicate<Employee> pred, Handler<AsyncResult<List<Employee>>> handler){employeeDao.getEmployeeByCol(pred, handler);}
    public void getListOfColValues(Function<Employee, String> lambda, Handler<AsyncResult<List<String>>> handler) {employeeDao.getListOfColValues(lambda, handler);}
    public void getSortedEmployees(Handler<AsyncResult<Map<String, Employee>>> handler){employeeDao.getSortedEmployees(handler);}
    public void removeEmployee(String id){employeeDao.removeEmployee(id);}
    public void removeAllEmployees() { employeeDao.removeAllEmployees(); }
    public void updateEmployee(Employee emp){employeeDao.updateEmployee(emp);}
    public void addEmployee(Employee emp) {employeeDao.addEmployee(emp);}

}
