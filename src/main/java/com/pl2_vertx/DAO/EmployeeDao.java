package com.pl2_vertx.DAO;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.error.BucketDoesNotExistException;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.pl2_vertx.config.DbConfig;
import com.pl2_vertx.dto.Employee;
import io.vertx.core.json.Json;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EmployeeDao {

    private static EmployeeDao empDao;
    public Cluster cl;
    public Bucket bucket;

    private EmployeeDao(){
        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                //this set the IO socket timeout globally, to 45s
                .socketConnectTimeout((int) TimeUnit.SECONDS.toMillis(45))
                //this sets the connection timeout for openBucket calls globally (unless a particular call provides its own timeout)
                .connectTimeout(TimeUnit.SECONDS.toMillis(60))
                .build();
        try {

            cl = CouchbaseCluster.create(env, DbConfig.url);

            cl.authenticate(DbConfig.username, DbConfig.password);
            bucket = cl.openBucket(DbConfig.bucket);
        } catch(BucketDoesNotExistException e){
            System.out.println("Bucket doesn't exist .... creating new one");

            ClusterManager clusterManager = cl.clusterManager();
            BucketSettings bucketSettings = new DefaultBucketSettings.Builder()
                    .type(BucketType.COUCHBASE)
                    .name(DbConfig.bucket)
                    .quota(120)
                    .build();

            clusterManager.insertBucket(bucketSettings);
            bucket = cl.openBucket(DbConfig.bucket);
            bucket.query(N1qlQuery.simple("create primary index employees_index on "+DbConfig.bucket));
        } catch (Exception e ){
            System.out.println("unable to access the couchbase server please check the configuration");
        }

    }

    public static EmployeeDao getService(){
        if(empDao == null){
            empDao = new EmployeeDao();
        }
        return empDao;
    }

    public void addEmployee(Employee emp){
        JsonObject jsonEmp = JsonObject.fromJson(Json.encodePrettily(emp));
        try {
            bucket.insert(JsonDocument.create(emp.getEmpId(), jsonEmp));
        } catch(Exception e){
            System.out.println("Add Employee Failed. CAUSE: " + e.getMessage());
        }
    }

    public Employee getOneEmployee(String id){
        JsonDocument doc = bucket.get(id);
        if (doc == null)
            return null;

        return Json.decodeValue(doc.content().toString(),Employee.class);
    }

    public Map<String, Employee> getAllEmployees() {
        N1qlQueryResult result = bucket.query(N1qlQuery.simple("select * from Employees"));

        return result.allRows().stream()
                .map(e->JsonObject.fromJson(e.toString()).get("Employees").toString())
                .map(e->Json.decodeValue(e,Employee.class))
                .collect(Collectors.toMap(e->e.getEmpId(), e->e));

    }

    public Map<String, Employee> getSortedEmployees(){
        N1qlQueryResult result = bucket.query(N1qlQuery.simple("select * from Employees"));

        return result.allRows().stream()
                .map(e->JsonObject.fromJson(e.toString()).get("Employees").toString())
                .map(e->Json.decodeValue(e, Employee.class))
                .sorted(Comparator.comparing(Employee::getName))
                .collect(Collectors.toMap(e->e.getEmpId(), e->e, (e1,e2) -> e1, LinkedHashMap::new));
    }

    public Employee getEmployeeByCol(Predicate<Employee> pred){
        N1qlQueryResult result = bucket.query(N1qlQuery.simple("select * from Employees"));
        List<Employee> list = result.allRows().stream()
                .map(e->JsonObject.fromJson(e.toString()).get("Employees").toString())
                .map(e->Json.decodeValue(e, Employee.class))
                .filter(pred)
                .collect(Collectors.toList());

        // Returns null if employee with value doesnt exist
        if(list.isEmpty())
            return null;

        // Return value which is the first item from list.
        return list.get(0);
    }

    public List<String> getListOfColValues(Function<Employee, String> lambda){
        N1qlQueryResult result = bucket.query(N1qlQuery.simple("select * from Employees"));

        return result.allRows().stream()
                .map(e->JsonObject.fromJson(e.toString()).get("Employees").toString())
                .map(e->Json.decodeValue(e, Employee.class))
                .map(lambda)
                .collect(Collectors.toList());
    }

    public void removeEmployee(String id){
        try {
            bucket.remove(id);
        } catch(Exception e) {
            System.out.println("Remove Employee Failed. CAUSE: " + e.getMessage());
        }
    }

    public void removeAllEmployees(){
        try {
            N1qlQueryResult result = bucket.query(N1qlQuery.simple("select empId from Employees"));
            result.allRows().stream()
                    .map(e -> JsonObject.fromJson(e.toString()).get("empId"))
                    .forEach(e -> bucket.remove(e.toString()));
        } catch(Exception e) {
            System.out.println("Remove All Employees Failed. CAUSE: " + e.getMessage());
        }
    }

    public void updateEmployee(Employee emp){
        JsonObject jsonEmp = JsonObject.fromJson(Json.encodePrettily(emp));
        try {
            bucket.replace(JsonDocument.create(emp.getEmpId(), jsonEmp));
        } catch(Exception e){
            System.out.println("Employee Update Failed. CAUSE: " + e.getMessage());
        }
    }

}
