package com.pl2_vertx.DAO;

import com.pl2_vertx.dto.Employee;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EmployeeDaoTest {
    public static Map<String, Employee> emp = new LinkedHashMap<>();

    public Employee getEmployee(String id) {
        return emp.get(id);
    }

    public Map<String, Employee> getEmployees(){

        return emp;
    }


    public  EmployeeDaoTest(){

        Employee employee1 = new Employee();
        employee1.setName("Hunk Lawrence");
        emp.put(employee1.getEmpId(),employee1);

        Employee employee2 = new Employee();
        employee2.setName("Keith Bell");
        emp.put(employee2.getEmpId(),employee2);

        Employee employee3 = new Employee();
        employee3.setName("John Miller");
        emp.put(employee3.getEmpId(),employee3);

    }

    public void addEmployee(Employee e){
        emp.put(e.getEmpId(),e);
    }
    public void updateEmployee(Employee e){

        emp.put(e.getEmpId(),e);
    }
    public void deleteEmployee(String id){
        emp.remove(id);
    }
}
