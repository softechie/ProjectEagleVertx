package com.pl2_vertx.service;

import com.pl2_vertx.DAO.EmployeeDaoTest;
import com.pl2_vertx.dto.Employee;

import java.util.List;
import java.util.Map;

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
}



