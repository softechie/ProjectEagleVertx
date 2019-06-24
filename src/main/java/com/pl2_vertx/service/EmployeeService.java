package com.pl2_vertx.service;

import com.pl2_vertx.DAO.EmployeeDao;
import com.pl2_vertx.dto.Employee;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

// singelton
public class EmployeeService {

    private EmployeeDao employeeDao = EmployeeDao.getService();

    public void addEmployee(Employee emp){employeeDao.addEmployee(emp);}
    public Employee getOneEmployee(String id){return employeeDao.getOneEmployee(id);}
    public Map<String, Employee> getAllEmployees(){return employeeDao.getAllEmployees();}
    public Employee getEmployeeByCol(Predicate<Employee> pred){return employeeDao.getEmployeeByCol(pred);}
    public List<String> getListOfColValues(Function<Employee, String> lambda) {return employeeDao.getListOfColValues(lambda);}
    public Map<String,Employee> getSortedEmployees() { return employeeDao.getSortedEmployees();}
    public void removeEmployee(String id){employeeDao.removeEmployee(id);}
    public void removeAllEmployees() { employeeDao.removeAllEmployees(); }
    public void updateEmployee(Employee emp){employeeDao.updateEmployee(emp);}

}
