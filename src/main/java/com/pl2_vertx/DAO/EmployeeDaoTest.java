package com.pl2_vertx.DAO;

import com.pl2_vertx.dto.Employee;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EmployeeDaoTest {
	//IDIOM:Item-0013: Prefer Collections over Arrays
    public static Map<String, Employee> emp = new LinkedHashMap<>();

    public Employee getEmployee(String id) {
        return emp.get(id);
    }

    public Map<String, Employee> getEmployees(){ return emp; }

    public EmployeeDaoTest(){

        Employee employee1 = new Employee();
        employee1.setName("Zunk Lawrence");
        employee1.setEmail("HL@gmail.com");
        employee1.setPhone("1235414513");
        employee1.setTenure("2");
        employee1.setAcctid("5");
        employee1.setCl("CA");
        employee1.setDoj("2019-01-10");
        employee1.setHl("AZ");
        employee1.setRmid("6");
        employee1.setRoleid("4");
        employee1.setStatus("T");
        employee1.setWl("CH");
        emp.put(employee1.getEmpId(),employee1);

        Employee employee2 = new Employee();
        employee2.setName("Keith Bell");
        employee2.setEmail("KB@gmail.com");
        employee2.setPhone("9998884343");
        employee2.setTenure("1");
        employee2.setAcctid("6");
        employee2.setCl("AC");
        employee2.setDoj("2017-01-10");
        employee2.setHl("ID");
        employee2.setRmid("9");
        employee2.setRoleid("3");
        employee2.setStatus("NA");
        employee2.setWl("KA");
        emp.put(employee2.getEmpId(),employee2);

        Employee employee3 = new Employee();
        employee3.setName("John Miller");
        employee3.setEmail("JM@gmail.com");
        employee3.setPhone("1324232211");
        employee3.setTenure("3");
        employee3.setAcctid("6");
        employee3.setCl("NV");
        employee3.setDoj("2018-01-10");
        employee3.setHl("OR");
        employee3.setRmid("7");
        employee3.setRoleid("3");
        employee3.setStatus("F");
        employee3.setWl("HA");
        emp.put(employee3.getEmpId(),employee3);

    }

    public void addEmployee(Employee e){
        emp.put(e.getEmpId(),e);
    }
    public void updateEmployee(Employee e){ emp.put(e.getEmpId(),e); }
    public void deleteEmployee(String id){
        emp.remove(id);
    }
    public void deleteAllEmployees(){ emp.clear(); }

    public List<String> getListOfColValues(Function<Employee,String> lambda){
        return emp.values()
                .stream()
                .map(lambda)
                .collect(Collectors.toList());
    }

    public Employee getEmployeeByCol(Predicate<Employee> pred){
       List<Employee> list = emp.values()
                .stream()
                .filter(pred).collect(Collectors.toList());

       // Returns null if employee with value doesnt exist
       if(list.isEmpty())
           return null;

       // Return value which is the first item from list.
       return list.get(0);
    }

    public Map<String,Employee> getSortedEmployees(){
        return emp.values()
                .stream()
                .sorted(Comparator.comparing(Employee::getName))
                .collect(Collectors.toMap(e->e.getEmpId(), e->e, (e1,e2)-> e1, LinkedHashMap::new));
    }

}
