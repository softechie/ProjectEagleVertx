package DAO;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.bucket.BucketType;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.CouchbaseEnvironment;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.error.BucketDoesNotExistException;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Statement;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;
import config.DbConfig;
import dto.Employee;
import io.vertx.core.json.Json;

import javax.naming.ConfigurationException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.couchbase.client.java.query.dsl.Expression.i;

// singelton
public class EmployeeService {
    private static EmployeeService empservice;
    public  Cluster cl;
    public Bucket bucket;
    private EmployeeService(){
        CouchbaseEnvironment env = DefaultCouchbaseEnvironment.builder()
                //this set the IO socket timeout globally, to 45s
                .socketConnectTimeout((int) TimeUnit.SECONDS.toMillis(45))
                //this sets the connection timeout for openBucket calls globally (unless a particular call provides its own timeout)
                .connectTimeout(TimeUnit.SECONDS.toMillis(60))
                .build();
        try {

            cl = CouchbaseCluster.create(env,DbConfig.url);

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
            System.out.println("unable to acces the couchbase server please check the configuration");
        }

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
