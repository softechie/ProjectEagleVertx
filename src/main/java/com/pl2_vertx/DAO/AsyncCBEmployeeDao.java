package com.pl2_vertx.DAO;

import com.couchbase.client.java.*;
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
import com.couchbase.client.java.query.Statement;
import com.pl2_vertx.config.DbConfig;
import com.pl2_vertx.dto.Employee;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import rx.Subscriber;
import rx.functions.Action1;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.couchbase.client.java.query.Select.select;

public class AsyncCBEmployeeDao {

    private static AsyncCBEmployeeDao empDao;
    public AsyncCluster cl;
    public AsyncBucket bucket;

    private AsyncCBEmployeeDao(){
        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                //this set the IO socket timeout globally, to 45s
                .socketConnectTimeout((int) TimeUnit.SECONDS.toMillis(45))
                //this sets the connection timeout for openBucket calls globally (unless a particular call provides its own timeout)
                .connectTimeout(TimeUnit.SECONDS.toMillis(60))
                .build();
        try {

            cl = CouchbaseAsyncCluster.create(env, DbConfig.url);
            cl.authenticate(DbConfig.username, DbConfig.password);
            
            //IDIOM:Item-0451: inline unnecessary variables
            cl.openBucket(DbConfig.bucket).subscribe(b -> bucket = b);

        } catch(BucketDoesNotExistException e){
            System.out.println("Bucket doesn't exist .... creating new one");

            ClusterManager clusterManager = (ClusterManager) cl.clusterManager();
            BucketSettings bucketSettings = new DefaultBucketSettings.Builder()
                    .type(BucketType.COUCHBASE)
                    .name(DbConfig.bucket)
                    .quota(120)
                    .build();

            clusterManager.insertBucket(bucketSettings);
            cl.openBucket(DbConfig.bucket).subscribe(b -> bucket = b);
            bucket.query(N1qlQuery.simple("create primary index employees_index on "+DbConfig.bucket));
        } catch (Exception e ){
            System.out.println("unable to access the couchbase server please check the configuration");
        }

    }

    public static AsyncCBEmployeeDao getService(){
        if(empDao == null){
            empDao = new AsyncCBEmployeeDao();
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

    public void getOneEmployee(String id, RoutingContext routingContext){
        bucket.get(id).subscribe(res -> routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(res.content().toString()));
    }

    public void getAllEmployees(RoutingContext routingContext) {
        Statement r = select("*").from("Employees");
        N1qlQuery n1qlQuery = N1qlQuery.simple(r);
        //Map<String, Employee> emps = new HashMap<>();

        bucket.query(n1qlQuery).flatMap(res -> res.rows())
                .map(row -> row.value())
                .map(e->e.toString())
                /*.map(e->JsonObject.fromJson(e.toString()).get("Employees").toString())*/
                .forEach(e -> System.out.println(e));
                /*.map(e->Json.decodeValue(e,Employee.class))*/

       /*         .subscribe(new Subscriber<Employee>() {
                    Map<String, Employee> list = new HashMap<>();

                    @Override
                    public void onCompleted() {
                        routingContext.response()
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .end(Json.encodePrettily(list));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println(throwable.getMessage());
                    }

                    @Override
                    public void onNext(Employee employee) {
                        list.put(employee.getEmpId(), employee);
                    }
                });*/
/*

          routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(""));
*/



/*
        //Extract rows from Couchbase WITHOUT streams and lambdas.
        Map<String, Employee> employeeMap = new HashMap<String, Employee>();
        Iterator<N1qlQueryRow> itr = result.rows();
        while(itr.hasNext()){
            JsonObject empJO = JsonObject.fromJson(itr.next().toString());
            Employee emp = Json.decodeValue(empJO.get("Employees").toString(), Employee.class);
            employeeMap.put(emp.getEmpId(), emp);
        }
        return employeeMap;

 */

      /*  long begin = System.nanoTime();
        //Extract all rows from Couchbase WITH streams and lambda
        Map<String, Employee> emp = result.allRows().stream()
                .map(e->JsonObject.fromJson(e.toString()).get("Employees").toString())
                .map(e->Json.decodeValue(e,Employee.class))
                .collect(Collectors.toMap(e->e.getEmpId(), e->e));
        long end = System.nanoTime();
        //System.out.println("Stm: " + (end-begin) / 1000000);*/
    }


    public Map<String, Employee> getSortedEmployees(){
        N1qlQueryResult result = (N1qlQueryResult) bucket.query(N1qlQuery.simple("select * from Employees"));
        Statement r = select("*").from("Employees");
        N1qlQuery n1qlQuery = N1qlQuery.simple(r);

        return result.allRows().stream()
                .map(e->JsonObject.fromJson(e.toString()).get("Employees").toString())
                .map(e->Json.decodeValue(e, Employee.class))
                .sorted(Comparator.comparing(Employee::getName))
                .collect(Collectors.toMap(e->e.getEmpId(), e->e, (e1,e2) -> e1, LinkedHashMap::new));
    }

    public Employee getEmployeeByCol(Predicate<Employee> pred){
        N1qlQueryResult result = (N1qlQueryResult) bucket.query(N1qlQuery.simple("select * from Employees"));
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
        N1qlQueryResult result = (N1qlQueryResult) bucket.query(N1qlQuery.simple("select * from Employees"));

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
            N1qlQueryResult result = (N1qlQueryResult) bucket.query(N1qlQuery.simple("select empId from Employees"));
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
