package com.pl2_vertx.service;

import com.pl2_vertx.DAO.EmployeeDaoTest;
import com.pl2_vertx.dto.Employee;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class TestService {

    private EmployeeDaoTest employeeDaoTest = new EmployeeDaoTest();

    public Map<String,Employee> getEmployees() {
        return employeeDaoTest.getEmployees();
    }

    public Employee getEmployee(String id) {
        return employeeDaoTest.getEmployee(id);
    }
    public void addEmployee(Employee e){employeeDaoTest.addEmployee(e);}
    public void updateEmployee(Employee e){employeeDaoTest.updateEmployee(e);}
    public void deleteEmployee(String id){employeeDaoTest.deleteEmployee(id);}
    public Employee getEmployeeByCol(Predicate<Employee> pred){return employeeDaoTest.getEmployeeByCol(pred);}
    public Map<String,Employee> getSortedEmployees() { return employeeDaoTest.sortedEmployees();}
    public void deleteAllEmployees() { employeeDaoTest.deleteAllEmployees(); }
    public List<String> getListOfColValue(Function<Employee, String> lambda) { return employeeDaoTest.getListOfColValue(lambda);}
}



