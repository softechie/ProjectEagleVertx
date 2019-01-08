package DAO;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;
import dto.Employee;
import io.vertx.core.json.Json;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.couchbase.client.java.query.dsl.Expression.i;

// singelton
public class EmployeeService {
    private static EmployeeService empservice;
    public  Cluster cl;
    public Bucket bucket;
    private EmployeeService(){

             cl = CouchbaseCluster.create("couchbase");
            cl.authenticate("Administrator", "password");
            bucket=cl.openBucket("Employees");

    }

    public static EmployeeService getService(){
        if(empservice == null){
            empservice = new EmployeeService();
        }
        return empservice;
    }

    public void addEmployee(Employee emp){
        JsonObject jsonEmp = JsonObject.fromJson(Json.encodePrettily(emp));
               bucket.insert(JsonDocument.create(emp.getEmpId(), jsonEmp));
    }

    public  Map<String, Employee> getAllEmployees() {
        Map<String, Employee> employees = new LinkedHashMap<>();

        N1qlQueryResult result = bucket.query(N1qlQuery.simple("select * from Employees"));
        for (N1qlQueryRow row : result) {
           JsonObject jbs = JsonObject.fromJson(row.toString());
          Employee emp =  Json.decodeValue(jbs.get("Employees").toString(),Employee.class);
          employees.put(emp.getEmpId(),emp);

        }
        return employees;

    }
    public Employee getOneEmployee(String id){
        JsonDocument doc = bucket.get(id);
        if (doc == null)
            return null;
        return Json.decodeValue(doc.content().toString(),Employee.class);
    }

    public void removeEmployee(String id){

        bucket.remove(id);
    }

    public void updateEmployee(Employee emp){
        JsonObject jsonEmp = JsonObject.fromJson(Json.encodePrettily(emp));

        bucket.replace(JsonDocument.create(emp.getEmpId(), jsonEmp));

    }



}
